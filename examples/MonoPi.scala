/*
 * @author Jiansen HE
 * 
 */

import join._
import scala.concurrent.ops._ //spawn


/*
 *   Encoding channels of monadic \pi-calculus, which takes one value at a time
 */
class MonoPi extends Join{
  object send extends AsyName[Int]
  object receive extends SynName[Unit, Int]
  
  join{
    case send(x) and receive(_) => receive reply x
  }
}

/*
 *   Encoding polyadic \pi-calclus into monadic \pi-calculus.
 *   
 *   [x(y_1, ... , y_n).P] => x(w).w(y_1) ... w(y_n).[P] 
 *   [\bar{x}(z_1, ... , z_n).P] => (new w) (\bar{x}<w>.\bar{w}<z_1> ... \bar{w}<z_n>.[P]) 
 *   
 *   Key: generating a unique channel w between senders and receivers
 */
class PolyPi extends Join{
  object send extends AsyName[List[Int]]
  object receive extends SynName[Unit, List[Int]]
  
  join{
    case send(l) and receive(_) =>
      val w = new MonoPi
      val ws = w.send
      val wr = w.receive
      
      for (i <- l) ws(i)
      
      val result = for (i <- 0 until l.size) yield wr()
      
      receive reply result.toList
  }
}

object MonoTest extends App{
  val pi = new PolyPi
  val s = pi.send
  val r = pi.receive 
  
  spawn {s(List(1,2,3,4,5) )}
  spawn {s(List(6,7,8,9,10) )}  
  spawn {println( r() )}// List(1, 2, 3, 4, 5)  or   List(6,7,8,9,10
}