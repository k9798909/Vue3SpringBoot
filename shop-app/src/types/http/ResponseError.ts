export default interface ResponseError<T> {
  message: string
  fieldErrors: T
}
