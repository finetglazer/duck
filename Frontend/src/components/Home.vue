<template>
<div class="d-flex align-items-center flex-wrap home">
    <div class="background-container">
        <!-- <img :src="banner" alt="" class="banner"> -->
        <video autoplay muted loop class="banner">
            <source :src="bannerVideo" type="video/mp4" />
        </video>
      
    </div>
    <div class="zoom">
        <div>
            <img :src="logo" alt="logo" class="logo">
        </div>
        <router-link to="/zoom" class="play-btn">PLAY NOW</router-link>
        <div class="mt-5 d-flex flex-column gap-3">
        <div class="animate">
            <img :src="dog" class="icon animate-dog" alt="">
            <img :src="duck" class="icon animate-duck" alt="">
        </div>

        </div>
    </div>
</div>

</template>

<script setup>
import { ref, onMounted } from "vue"
import bannerVideo from "../assets/video/race.mp4"
import duck from "../assets/img/duck-toy.png"
import dog from "../assets/img/dog.png"
import logo from "../assets/img/logo_2 .webp"
</script>

<style lang="css">
.home {
    height: calc(100vh - 80px);
    background-color: #56B4D3;
}

.background-container {
    width: 80%;
}

.zoom {
    width: 20%;
    text-align: center;
    height: 100%;
}

.banner {
    width: 100%;
    height: calc(100vh - 80px);
    object-fit: cover;
}


.play-btn {
    display: inline-block;
    margin-top: 40%;
    padding: 15px 50px;
    border-radius: 8px;
    border: 2px solid #fff;
    background: #FF512F;
    color: #fff;
    animation: blink 1.5s infinite alternate; 
    text-decoration: none;
}

.animate {
  width: 100%; 
  position: relative;
  overflow: hidden; 
  height: 300px; 
  background: url('../assets/img/riverriver.avif');
  margin-top: 20px;
}

@keyframes blink {
    0% {
        background-color: #FF512F; /* Start with red */
    }
    100% {
        background-color: #F4D03F; /* End with yellow */
    }
}

.play-btn:hover {
    opacity: 0.9;
}


.icon {
    width: 60px;
    height: 60px;
}

/* Animation for duck */
@keyframes moveHorizontally {
  0% {
    /* transform: translateX(0); */
    left: 5%;
  }
  100% {
    /* transform: translateX(100%);  */
    left: 80%;
  }
}

.animate-duck {
  /* animation: moveHorizontally 4s ease-in-out infinite alternate;  */
  animation: moveHorizontally 5s ease-in-out infinite; 
  position: absolute;
  top: 65%;
  left: 0;
  translate: translateY(-50%);
}

.animate-dog {
  animation: moveHorizontally 4s ease-in-out infinite; 
  position: absolute;
  top: 36%;
  left: 0;
  translate: translateY(-50%);
}

.logo {
    margin-top: 20px;
    width: 110px;
    height: 110px;
    object-fit: cover;
    border-radius: 50%;
}


/* .animate-duck {
  animation: moveHorizontally 5s linear infinite; 
} */
</style>
