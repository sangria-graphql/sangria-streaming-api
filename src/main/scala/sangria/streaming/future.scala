package sangria.streaming

import scala.language.higherKinds

import scala.concurrent.{ExecutionContext, Future}

object future {
  class FutureSubscriptionStream(implicit ec: ExecutionContext) extends SubscriptionStream[Future] {
    def supported[T[_]](other: SubscriptionStream[T]): Boolean =
      other.isInstanceOf[FutureSubscriptionStream]

    def map[A, B](source: Future[A])(fn: A => B): Future[B] = source.map(fn)

    def singleF[T](value: Future[T]): Future[T] = value

    def single[T](value: T): Future[T] = Future.successful(value)

    def mapF[A, B](source: Future[A])(fn: A => Future[B]): Future[B] =
      source.flatMap(fn)

    def first[T](s: Future[T]): Future[T] = s

    def failed[T](e: Throwable): Future[T] = Future.failed(e)

    def onComplete[Ctx, Res](result: Future[Res])(op: => Unit): Future[Res] =
      result
        .map { x => op; x }
        .recover { case e => op; throw e }

    def flatMap[Ctx, Res, T](future: Future[T])(resultFn: T => Future[Res]): Future[Res] =
      future.flatMap(resultFn)

    def merge[T](streams: Vector[Future[T]]): Future[T] = Future.firstCompletedOf(streams)

    def recover[T](stream: Future[T])(fn: Throwable => T): Future[T] =
      stream.recover { case e => fn(e) }
  }

  implicit def futureSubscriptionStream(implicit ec: ExecutionContext): FutureSubscriptionStream =
    new FutureSubscriptionStream
}
