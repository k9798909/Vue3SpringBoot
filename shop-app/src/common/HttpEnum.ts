export enum RequestEnum {
  GET = 'GET',
  POST = 'POST',
  PATCH = 'PATCH',
  PUT = 'PUT',
  DELETE = 'DELETE'
}


export enum ContentTypeEnum {
  // json
  JSON = 'application/json',
  // json
  TEXT = 'text/plain',
  // form-data
  FORM_URLENCODED = 'application/x-www-form-urlencoded',
  // form-data  upload
  FORM_DATA = 'multipart/form-data'
}

export enum NetworkErrorCode {
  Unauthorized = 401,
  Forbidden = 403,
  NotFound = 404,
  InternalServerError = 500,
  ServiceUnavailable = 503,
  GatewayTimeout = 504
}
