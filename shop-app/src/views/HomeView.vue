<script setup lang="ts"></script>

<template>
  <main>
    <section class="text-center">
      <h1 class="tagline">
        The
        <span class="accent">Progressive</span>
        <br />JavaScript Framework
      </h1>
      <p class="description">
        An approachable, performant and versatile framework for building web user interfaces.
      </p>
    </section>
    <section class="vt-box-container">
      <div class="vt-box">
        <h2>Approachable</h2>
        <p>
          Builds on top of standard HTML, CSS and JavaScript with intuitive API and world-class
          documentation.
        </p>
      </div>
      <div class="vt-box">
        <h2>Performant</h2>
        <p>
          Truly reactive, compiler-optimized rendering system that rarely requires manual
          optimization.
        </p>
      </div>
      <div class="vt-box">
        <h2>Versatile</h2>
        <p>
          A rich, incrementally adoptable ecosystem that scales between a library and a
          full-featured framework.
        </p>
      </div>
    </section>
  </main>
</template>

<style lang="scss" scoped>
.accent {
  background: -webkit-linear-gradient(315deg, #42d392 25%, #647eff);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.tagline {
  font-size: 76px;
  line-height: 1.25;
  font-weight: 900;
  letter-spacing: -1.5px;
  max-width: 960px;
  margin: 0 auto;
}

.vt-box-container {
  max-width: 960px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;

  h2 {
    display: block;
    font-size: 1.5em;
    margin-block-start: 0.83em;
    margin-block-end: 0.83em;
    font-weight: bold;
  }

  .vt-box {
    background-color: transparent;
    transition:
      color 0.5s,
      background-color 0.5s;
    padding: 28px 36px;
    border-radius: 8px;
    flex: 0 32%;
    font-size: 14px;
    font-weight: 500;
  }
}
</style>
