<script setup>
import Home from './components/Home.vue';
import Header from './components/Header.vue';
import Footer from './components/Footer/Footer.vue'
import { ref, onMounted, watch } from "vue"
const muted = ref(false);
const audio = ref(null);

const playAudio = () => {
  muted.value = true;
    if (audio.value && muted.value) {
        audio.value.play().then(() => {
          muted.value = true;
            console.log("Audio is playing.");
        }).catch((error) => {
            console.error("Audio play failed: ", error);
        });
    }
};

watch(muted, () => {
  if(!muted.value) {
    audio.value.pause();
  } else {
    playAudio();
  }
});

const handleMuted = (value) => {
  muted.value = value;
};



</script>

<template>
  <div>
    <audio ref="audio" loop style="display: none;">
          <source src="./assets/music/root_music.mp3" type="audio/mp3" />
    </audio>
    <Header :muted="muted" @muted="handleMuted"/>
    <router-view  @click="playAudio"></router-view>
    <Footer/>
  </div>
</template>

<style scoped>

</style>
