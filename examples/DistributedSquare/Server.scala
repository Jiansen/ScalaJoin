
/*
 * @author Jiansen HE
 * 
 */
 
 /* Listing 2 in the user manual.
 * 
 * The Server object defines a distributed synchronous channel, 
 * which return the square of the input integer.
 * 
 */

import join._
object Server extends App{//for scala 2.9.0
  val port = 9000
  object join extends DisJoin(port, 'JoinServer){
    object sq extends SynName[Int, Int]
    join{      case sq(x) => println("x:"+x); sq reply x*x    }
    registerChannel("square", sq)
  }
  join.start()
}