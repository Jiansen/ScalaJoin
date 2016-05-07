/*
 * The Surgery problem: 
 * ( http://www.doc.ic.ac.uk/~pg/Concurrency/4pi.pdf )
 * 
 * Senario:
 * A surgery consists of many doctors and one receptionist.  
 * It serves visiting patients as follows:
 * 
 * 1. a patient checks in at the reception desk, describes his/her symptom.
 * 2. when a doctor is ready, the receptionist gives him/her the next patient.
 * 3. the doctor gives prescription to the patient according to .
 * 
 * Following code demonstrates:
 * a. private channel communication (names of doctors and patients)
 * b. dynamic channel configuration 
 * 
 * @author Jiansen HE
 * 
 */


import join._
import scala.concurrent.ops._ //spawn

object Receptionist extends Join{
  object checkin extends AsyName[(AsyName[String] , String)]
  object next extends AsyName[AsyName[(AsyName[String] , String)]]
  
  join{
    case checkin(n,s) and next(a) =>
      println("Receptionist: Made appointment with Dr. "+a+" for patient "+n+".")
      a(n,s)
  }
}

class Doctor(name:String) extends Join{
  def pre(s:String):String = {
    "pre_"+s
  }
  
  private object a extends AsyName[(AsyName[String] , String)] {
    override def toString() = {
      name
    }
  }
  
  join {
    case a(n,s) =>
      println("Dr. "+name+": Dispensed a prescription to patient "+n+".")
      n(pre(s))
      Receptionist.next(a)
  }
  
  def work() = {  Receptionist.next(a) }
}

class Patient(n:String, sym:String) extends Join{
  private object name extends AsyName[String]{
    override def toString():String = {
      n
    }
  }
  
  join {
    case name(pre) => println("Patient: "+n+": Reciving prescription\t"+pre+".\n")
  }
  
  def goHospital() = {  Receptionist.checkin(name, sym) }
}


object surgery_demo extends App{
  object danny extends Doctor("Danny")
  object daryl extends Doctor("Daryl")
  object devo extends Doctor("Devo")
  
  danny.work
  daryl.work
  devo.work
  
  
  object pagan extends Patient("Pagan", "feverish")
  object pemberley extends Patient("Pemberley", "dizzy ")
  object piper extends Patient("Piper", "sleepy")
  object preston extends Patient("Preston", "thirsty")
  object pier extends Patient("Pier", "weak")
  object perrin extends Patient("Perrin", "tired")
  object piperel extends Patient("Piperel", "dystonia")
  
  spawn {pagan.goHospital}
  spawn {pemberley.goHospital}
  spawn {piper.goHospital}
  spawn {preston.goHospital}
  spawn {pier.goHospital}
  spawn {perrin.goHospital}
  spawn {piperel.goHospital}
}