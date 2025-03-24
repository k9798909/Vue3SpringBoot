<template>
  <v-text-field v-bind="$attrs" :errorMessages="localErrorMessage" @input="onInput"></v-text-field>
</template>

<script setup lang="ts">
import { ref, watchEffect } from 'vue'
import type { VTextField } from 'vuetify/lib/components/index.mjs'

type TextFieldProps = InstanceType<typeof VTextField>['$props']

interface Props extends /* @vue-ignore */ TextFieldProps {
  custonErrorMessage?: string | string[]
}

const props = defineProps<Props>()
const emits = defineEmits(['input'])
const localErrorMessage = ref()
const onInput = () => {
  emits('input')
  localErrorMessage.value = ''
}

watchEffect(() => {
  localErrorMessage.value = props.custonErrorMessage
})

defineOptions({ inheritAttrs: false })
</script>

<style scoped lang="scss"></style>
