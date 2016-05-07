/*
 * @author Jiansen HE
 * 
 */

import join._
import scala.concurrent.ops._ //spawn

/*
 * An unbounded integer buffer
 */
object IntBuffer extends Join{
  object Put extends AsyName[Int]
  object Get extends SynName[Unit, Int]
  
  join{
    case Put(x) and Get(_) => Get reply x
  }
}

//a test: shall display integers in random order
object Buffer_test extends App{
  spawn {IntBuffer.Put(1)}
  spawn {IntBuffer.Put(2)}
  spawn {println( IntBuffer.Get() )}
  spawn {println( IntBuffer.Get() )}
  spawn {IntBuffer.Put(3)}
  spawn {IntBuffer.Put(4)}
  spawn {println( IntBuffer.Get() )}
  spawn {println( IntBuffer.Get() )}
  spawn {IntBuffer.Put(5)}
  spawn {IntBuffer.Put(6)}
  spawn {println( IntBuffer.Get() )}
  spawn {println( IntBuffer.Get() )}
}