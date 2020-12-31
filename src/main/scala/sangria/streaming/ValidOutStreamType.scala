package sangria.streaming

import scala.annotation.implicitNotFound

@implicitNotFound(msg = "${Res} is invalid type for the resulting GraphQL type ${Out}.")
trait ValidOutStreamType[-Res, +Out]

object ValidOutStreamType extends LowPrioValidOutType {
  implicit def validSubclass[Res, Out](implicit ev: Res <:< Out) =
    valid.asInstanceOf[ValidOutStreamType[Res, Out]]
  implicit def validNothing[Out] = valid.asInstanceOf[ValidOutStreamType[Nothing, Out]]
  implicit def validOption[Res, Out](implicit ev: Res <:< Out) =
    valid.asInstanceOf[ValidOutStreamType[Res, Option[Out]]]
}

trait LowPrioValidOutType {
  val valid = new ValidOutStreamType[Any, Any] {}

  implicit def validSeq[Res, Out](implicit ev: Res <:< Out) =
    valid.asInstanceOf[ValidOutStreamType[Res, Seq[Out]]]
}
