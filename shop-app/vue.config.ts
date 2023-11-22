import { Configuration } from 'webpack'
import type { LoaderOptions } from 'sass-loader'
export default {
  css: {
    loaderOptions: {
      sass: {
        additionalData: `
            @import "@/assets/scss/_variables.scss";
            @import "@/assets/scss/_mixins.scss";
          `
      } as LoaderOptions
    }
  },

} as Configuration
