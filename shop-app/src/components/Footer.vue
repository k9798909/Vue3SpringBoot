<script setup lang="ts"></script>

<template>
  <footer class="footer">
    <div class="container">
      <p class="mb-1">Released under the MIT License.</p>
      <p class="mb-1">Copyright © 2014-2023 Evan You</p>
    </div>
  </footer>
</template>

<style lang="scss" scoped>
$font-color: #213547;
$bg-color: #ffffff;
.footer {
  border-top: 1px solid rgba(60, 60, 60, 0.12);
  background-color: $bg-color;
  padding: 1em 0em;
  color: $font-color;
}

.footer > .container {
  padding-right: 15px;
  padding-left: 15px;
  justify-content: center;
  align-items: center;
  display: flex;
  flex-direction: column;
  width: 100%;
}
</style>
