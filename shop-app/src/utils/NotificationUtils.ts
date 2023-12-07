import { useNotification } from '@kyvg/vue3-notification'

enum NotificationType {
  SUCCESS = 'success',
  ERROR = 'error',
  INFO = 'info',
  WARING = 'warn'
}

const notification = useNotification()
function showNotification(message: string, type: NotificationType) {
  notification.notify({
    title: message,
    type: type
  })
}

export function showSuccessNotification(message: string) {
  showNotification(message, NotificationType.SUCCESS)
}

export function showErrorNotification(message: string) {
  showNotification(message, NotificationType.ERROR)
}

export function showInfoNotification(message: string) {
  showNotification(message, NotificationType.INFO)
}

export function showWaringNotification(message: string) {
  showNotification(message, NotificationType.WARING)
}
