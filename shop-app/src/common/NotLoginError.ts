export default class NotLoginError extends Error {
  constructor(msg: string) {
    super(msg)

    // Set the prototype explicitly.
    Object.setPrototypeOf(this, NotLoginError.prototype)
  }
}
