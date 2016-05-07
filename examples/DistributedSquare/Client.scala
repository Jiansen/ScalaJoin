/*
 * @author Jiansen HE
 * 
 */
import join._

/*
 *   Listing 3 in the user manual.
 *   
 *   After Server.scala is started, try this example by
 *   
 *   > scala Client 5
 *   > scala Client 7
 *   ...
 *   
 */
object Client{
  def main(args: Array[String]) {
    val server = DisJoin.connect("myServer", 9000, 'JoinServer)
    //val c = new DisSynName[Int, String]("square", server)
    //java.lang.Error: Distributed channel initial error: 
    //        Channel square does not have type Int => java.lang.String.
    //...
    val c = new DisSynName[Int, Int]("square", server)//pass
    val x = args(0).toInt
    val sqr = c(x)
    println("sqr("+x+") = "+sqr)
    exit()
  }
}