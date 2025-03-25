import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

// Notifications
import Notifications from '@kyvg/vue3-notification'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

const vuetify = createVuetify({
  components,
  directives
})

//main.scss
import './assets/main.scss'
import '@mdi/font/css/materialdesignicons.css'
import '@fortawesome/fontawesome-free/css/all.css'
import { AxiosError } from 'axios'
import * as NotificationUtils from '@/utils/NotificationUtils.ts'
import { ViewMsg } from '@/common/MsgEnum.ts'

const app = createApp(App)

app.config.errorHandler = (error, instance, info) => {
  if (error instanceof AxiosError) {
    return
  }
  console.error('An error occurred:', error)
  console.log('In component instance:', instance)
  console.log('Error info:', info)
  NotificationUtils.showErrorNotification(ViewMsg.Error)
}

window.addEventListener('unhandledrejection', (event) => {
  console.error('unhandledrejection', event.reason)
  event.preventDefault()
})

app.use(router)
app.use(vuetify)
app.use(createPinia())
app.use(Notifications)
app.mount('#app')
