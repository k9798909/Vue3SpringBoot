export default interface ResponseError<T> {
  code:string
  message: string
  fieldErrors: T
}
