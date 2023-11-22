import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

//VueCookies
import VueCookies from 'vue-cookies'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
const vuetify = createVuetify({
  components,
  directives
})

//main.css
import './assets/main.css'
import '@mdi/font/css/materialdesignicons.css'
import '@fortawesome/fontawesome-free/css/all.css'

const app = createApp(App)
app.use(router)
app.use(vuetify)
app.use(createPinia())
app.use(VueCookies, { expires: '1h', path: '/', domain: '', secure: true, sameSite: 'Lax' })
app.mount('#app')
