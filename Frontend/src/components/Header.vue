<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-primary">
    <div class="container-fluid">
      <img :src="duck" class="icon" alt="">
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link class="nav-link text-white" to="/">Home</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link text-white" to="/team">Team</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link text-white" to="/project">
              Project
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link text-white" to="/progress">Progress</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link text-white" to="/zoom">Zoom</router-link>
          </li>
        </ul>
        <div class="d-flex">
          <a target="_blank" href="https://github.com/jnp2018/mid-project-419084084" class="me-4 text-white">
            <i class="bi bi-github"></i>
          </a>
          <a target="_blank" href="https://www.facebook.com/doanchinhit2102" class="me-4 text-white">
            <i class="bi bi-facebook"></i>
          </a>
          <!-- <a href="#" class="me-4 text-white">
            <i class="bi bi-arrow-clockwise"></i>
          </a> -->
          <a href="#" class="text-white">
            <i class="bi bi-moon"></i>
          </a>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import duck from "../assets/img/duck-toy.png"
</script>
<style lang="scss" scoped>
.navbar {
  background-color: #6f42c1; 
  height: 80px;
}

.icon {
  width: 30px;
  height: 30px;
  margin-right: 10px;
}
</style>
