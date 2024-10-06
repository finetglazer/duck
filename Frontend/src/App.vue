<script setup>
import Home from './components/Home.vue';
import Header from './components/Header.vue';
import Footer from './components/Footer/Footer.vue'
</script>

<template>
  <div>
    <Header/>
    <router-view></router-view>
    <Footer/>
  </div>
</template>

<style scoped>

</style>
