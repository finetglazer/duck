<template>
    <div class="d-flex gap-3">
        <div class="zoom mt-2">
            <div class="d-flex justify-content-between">
                <p class="text-info mx-3" style="font-size: 22px; font-weight: 600">Hello, {{ nickname ? nickname : "Player" }}</p>
                <span class="badge rounded-pill text-bg-warning text-white" style="min-width: 80px; max-height: 30px; font-size: 18px; cursor: pointer" @click="showModal = true"><i class="bi bi-pencil-square"></i> Set name</span>
            </div>
            <div class="card" style="">
                <div class="card-body">
                    <h5 class="card-title title_zoom text-primary">
                        RACE DUCK
                        <img :src="duck" class="card-img-top icon_race" alt="...">
                    </h5>
                    <p class="card-text"> Player bet on the duck you want to choose</p>
                    <a href="/zoom/zoom-duck" target="_blank" class="btn btn-primary">Join zoom</a>
                </div>
            </div>
            <div class="card" style="margin-top: 20px">
                <div class="card-body">
                    <h5 class="card-title title_zoom text-warning">
                        DOG RACE
                        <img :src="dog" class="card-img-top icon_race" alt="...">
                    </h5>
                    <p class="card-text">Player bet on the dog you want to choose</p>
                    <a href="/zoom/zoom-duck" class="btn btn-warning">Join zoom</a>
                </div>
            </div>
            <div class="card" style="margin-top: 20px">
                <div class="card-body">
                    <h5 class="card-title title_zoom text-danger">
                        TURTLE RACE
                        <img :src="turtle" class="card-img-top icon_race" alt="...">
                    </h5>
                    <p class="card-text">Player bet on the turtle you want to choose</p>
                    <a href="/zoom/zoom-duck" class="btn btn-danger">Join zoom</a>
                </div>
            </div>
            <div class="card" style="margin-top: 20px">
                <div class="card-body">
                    <h5 class="card-title title_zoom text-success">
                        CAMEL RACE
                        <img :src="camel" class="card-img-top icon_race" alt="...">
                    </h5>
                    <p class="card-text">Player bet on the camel you want to choose</p>
                    <a href="/zoom/zoom-duck" class="btn btn-success">Join zoom</a>
                </div>
            </div>
        </div>
        <div class="ranking">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title title_zoom text-primary">
                        RANKING
                        <img :src="rank" class="card-img-top icon_race" alt="...">
                    </h5>
                    <p class="card-text d-flex justify-content-between" v-for="(user, index) in players" :key="index">
                        <span class="text-warning" style="font-weight: 600;" v-if="user.name !== nickname">
                            <span class="text-danger">#{{ index + 1}}.</span>
                            {{ user.name }} 
                        </span>
                        <span class="text-danger" style="font-weight: 600;" v-else>
                            <span>#{{ index + 1}}.</span>
                            {{ user.name }} 
                        </span>
                        <span class="badge rounded-pill text-bg-info text-white d-flex gap-1" style="min-width: 80px; max-height: 30px; font-size: 18px;">
                            {{ user.points }} 
                            <img :src="dollar" class="card-img-top icon-point" alt="...">
                        </span>
                    </p>
                </div>
            </div>
        </div>
    </div>
    
    <div class="modal modal-overlay" :class="{ show: showModal }"  aria-labelledby="exampleModalLabel" aria-hidden="true" tabindex="-1" style="display: block" v-if="showModal">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                <h5 class="modal-title" style="color: brown;"><i class="bi bi-gear-wide-connected" style="font-size: 27px;"></i> Set your nickname</h5> 
                <button
                    type="button"
                    class="btn-close"
                    @click="closeModal"
                    aria-label="Close"
                ></button>
                </div>
                <div class="modal-body">
                    <div class="form-floating mb-3">
                        <input v-model="nickname" class="form-control" id="floatingInput" placeholder="david">
                        <label for="floatingInput">nickname</label>
                    </div>
                </div>
                <div class="modal-footer">
                <button
                    type="button"
                    class="btn btn-secondary"
                    @click="closeModal"
                >
                    Close
                </button>
                <button type="button" class="btn btn-primary" @click="renameAction(nickname)">Save changes</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import duck from "../../assets/img/duck-toy.png"
import turtle from "../../assets/img/turtle.png"
import camel from "../../assets/img/camel _2.png"
import dog from "../../assets/img/dog.png"
import rank from "../../assets/img/ranking.png"
import dollar from "../../assets/img/dollar.png"
const players = ref([]);
const showModal = ref(false);
const nickname = ref("");
const cnt = 0;
const ip = ref("");

const closeModal = () => {
    showModal.value = false;
}

const filterPlayers = (arr) => {
    const res = [];
    arr = JSON.parse(arr);
    for (let i = 0; i < arr.length; i++) {
        if(arr[i]) {
            res.push(arr[i]);
        }
    }    
    return res;
}

const socket = new WebSocket('wss://pure-caverns-67534-35c6a327ed88.herokuapp.com/duckRace');
socket.addEventListener('message', function (event) {
    console.log(event.data);
    if(event.data.startsWith("PLAYER_ID")) {
        console.log(event.data.split(":")[1]);
        
        localStorage.setItem("player_id", event.data.split(":")[1]);
    } else {
        const users = JSON.parse(event.data);
        players.value = users.players.sort((a, b) => b.points - a.points);
    }
});


socket.addEventListener('close', function (event) {
    console.log('WebSocket connection closed');
});

function renameAction(nickname) {
    if (socket.readyState === WebSocket.OPEN) {
        console.log("Sending message to server...");
        socket.send(`SET_NAME:${nickname}`);
    } else {
        console.error('WebSocket is not open');
    }
    showModal.value = false;
}

</script>

<style lang="scss" scoped>
.icon_race {
    width: 50px;
    height: 50px;
    object-fit: cover;
}

.title_zoom {
    font-weight: 600;
}

.zoom {
    text-align: left;
    width: 60%;
}

.ranking {
    width: 40%;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.8); 
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1050;
}

.icon-point {
    width: 20px;
    height: 20px;
}
</style>