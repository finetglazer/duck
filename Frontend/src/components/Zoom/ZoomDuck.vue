<template>
    <div class="zoom_duck">
        <video autoplay loop muted preload="auto" playsinline class="background-video">
            <source src="../../assets/video/river.mp4" type="video/mp4">
        </video>
        <div class="img-rank">
            <img :src="bxh" class="card-img-top" @click="showRanking = true" alt="...">
        </div>

        <button class="btn btn-warning btn-bet" @click="showModal = true" v-if="remainTime > 1">Set bet</button>
        <p class="remain_time" v-if="remainTime > 1 && statusRace == 'pending'">{{ remainTime }}</p>
        <img v-if="remainTime <= 1 && statusRace == 'pending'" :src="progress" class="card-img-top icon_progress" alt="..."> 
        <img v-if="remainTime > 24 && statusRace == 'loss'" :src="loss" class="card-img-top icon_progress" alt="..."> 
        <img v-if="remainTime > 24 && statusRace == 'win'" :src="win" class="card-img-top icon_progress" alt="..."> 

        <p class="point">
            <img :src="wallet" class="card-img-top icon-point" alt="...">
            wallet: <strong class="text-warning">{{ point }}</strong> 
            <img :src="dollar" class="card-img-top icon-point" alt="...">
        </p>

        <div class="d-flex flex-column duck_block">
            <div v-for="(duckk, index) in ducks" :key="index" class="duck_parent" :style="{ animationDuration: duckk.duration + 's' }" :class="{ running: ducksRunning }">
                <img :src="duck" class="card-img-top icon_race" alt="...">
                <span class="duck_num">{{ index + 1 }}</span>
            </div>
        </div>
    </div>
       
   

    <!-- Ranking Modal Dialog -->
    <div class="modal modal-overlay" :class="{ show: showRanking }" v-if="showRanking">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title title_zoom text-primary">
                        RANKING
                        <img :src="rank" class="card-img-top icon_race" alt="...">
                    </h5>
                    <button type="button" class="btn-close" @click="closeRanking" aria-label="Close"></button>
                </div>
                <div class="modal-body">
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


    <!-- <div class="ranking">
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
    </div> -->

    <div class="modal modal-overlay" :class="{ show: showModal }"  aria-labelledby="exampleModalLabel" aria-hidden="true" tabindex="-1" style="display: block" v-if="showModal">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                <h5 class="modal-title" style="color: brown;"><i class="bi bi-check2-circle" style="font-size: 27px;"></i> Choose duck</h5> 
                <button
                    type="button"
                    class="btn-close"
                    @click="closeModal"
                    aria-label="Close"
                ></button>
                </div>
                <div class="modal-body">
                    <div class="form-floating mb-3">
                        <input v-model="duck_select" class="form-control" id="floatingInput" placeholder="david">
                        <label for="floatingInput">Select duck(1-5)</label>
                    </div>

                    <div class="form-floating mt-3">
                        <input v-model="amount" class="form-control" id="floatingInput" placeholder="david">
                        <label for="floatingInput">Bet amount</label>
                    </div>
                </div>
                <div class="modal-footer">
                <button
                    type="button"
                    class="btn btn-secondary"
                    @click="closeModal"
                >
                    Cancel
                </button>
                <button type="button" class="btn btn-primary" @click="setBet(duck_select, amount)">Set bet</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import duck from "../../assets/img/duck-toy.png"
import wallet from "../../assets/img/wallet.png"
import progress from "../../assets/img/game_progress_white_bg.png"
import loss from "../../assets/img/loss.png"
import win from "../../assets/img/win.png"
import rank from "../../assets/img/ranking.png"
import dollar from "../../assets/img/dollar.png"
import bxh from "../../assets/img/bxh.png"
import { ref, watch } from "vue";
import { toast } from "vue3-toastify"
import router from "../../router"
const duck_select = ref();
const amount = ref();
const showModal = ref(false);
const remainTime = ref(0);
const statusRace = ref("pending");
const point = ref();
const ducksRunning = ref(false);
const player_id = localStorage.getItem("player_id");
const players = ref([])
const ducks = ref([]);
const showRanking = ref(false); 

const closeRanking = () => {
    showRanking.value = false;
};

const socket = new WebSocket(`wss://pure-caverns-67534-35c6a327ed88.herokuapp.com/duckRace/race?playerId=${player_id}`);
socket.addEventListener('open', function (event) {
    console.log('Connected to WebSocket server duckRace/race');
});

socket.addEventListener('message', function (event) {
    // console.log(event.data);
    if(JSON.parse(event.data).type === "timer") {
       remainTime.value = JSON.parse(event.data).remainingTime;
       if(remainTime.value === 1) {
           ducksRunning.value = true;
       }

       if(remainTime.value < 30 && remainTime.value > 1) {
           ducksRunning.value = false;
       }

       if(remainTime.value == 25) {
        statusRace.value = "pending";
       }
    } else if(JSON.parse(event.data).type === "points") {        
        point.value = JSON.parse(event.data).points;
    } else if(JSON.parse(event.data).type === "noWinners") {
        statusRace.value = "loss";
    }  else if(JSON.parse(event.data).type === "raceFinished") {
        statusRace.value = "win";
    } else if(JSON.parse(event.data).type === "playerUpdate") {
        // players.value = JSON.parse(event.data).players;
        players.value = JSON.parse(event.data).players.sort((a, b) => b.points - a.points);
        const player =  JSON.parse(event.data).players.find(player => player.id === player_id);
        if (player) {
            point.value = player.points;
        }
    } else if(JSON.parse(event.data).type === "error") {
        toast.error("Player banned!Please Join zoom again!!!");
        setTimeout(() => {
            router.push("/zoom");
        }, 3000);
    } else if(JSON.parse(event.data).type === "raceFinishTimes") {
        console.log(JSON.parse(event.data).finishTimes, typeof(JSON.parse(event.data).finishTimes));
        const data = JSON.parse(event.data).finishTimes;
        const arr = [];
        if (typeof data === 'object' && data !== null) {
            arr.push(...Object.values(data).map((time) => ({ 
                duration: time
            }))); 
            ducks.value = arr;
        } else {
            console.error("Dữ liệu không phải là đối tượng.");
        }
        
    } else {
        // console.log("no non nonono");
    }
    
});

const closeModal = () => {
    showModal.value = false;
}

function setBet(duck_select, amount) {
    if (socket.readyState === WebSocket.OPEN) {
        console.log("Sending message to server...");
        const sendInfo = {
            "type": "placeBet",
            "candidateId": duck_select,
            "amount": amount,
        }
        socket.send(JSON.stringify(sendInfo));
        toast.success(`Set bet ${amount}$ vs number ${duck_select} successfully!`);
    } else {
        console.error('WebSocket is not open');
        toast.success("Set bet fail! Please try again!");
    }
    showModal.value = false;
}

</script>

<style lang="css" scoped>
.point {
    position: absolute;
    right: 0;
    top: 0;
    padding: 10px;
    background: brown;
    color: #fff;
}

.duck_block {
    position: absolute;
    top:260px;
}

.duck_parent {
    position: relative;
}

.duck_num {
    position: absolute;
    left: 0;
    top: 50%;
    display: block;
    width: 30px;
    height: 30px;
    background: #fff;
    border-radius: 50%;
    text-align: center;
}

.icon-point {
    width: 20px;
    height: 20px;
}

.btn-bet {
    position: absolute;
    top: 0;
    left: 0;
}

.img-rank {
    width: 50px;
    height: 50px;
    object-fit: cover;
    position: absolute;
    top: 0;
    right: 200px;
    cursor: pointer;
}

.zoom_duck {
    position: relative;
    width: calc(100vw - 10px);
    height: calc(100vh - 100px);
    object-fit: cover;
    /* background-image: url("../../assets/img/riverriver.png");
    background-repeat: no-repeat;
    background-size: cover; 
    background-position: center;  */
}

.background-video {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    animation: fadeIn 1s linear;
    z-index: -1;
}

@keyframes fadeIn {
    from { opacity: 0; }
    to { opacity: 1; }
}


.remain_time {
    position: absolute;
    top: 10px;
    left: 48%;
    transform: translateX(-50%);
    width: 70px;
    height: 70px;
    border-radius: 50%;
    color: #fff;
    font-size: 24px;
    background: #000;
    text-align: center;
    line-height: 70px;
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

.icon_race {
    margin-top: 12px;
    width: 95px;
    height: 95px;
    object-fit: cover;
}

.icon_progress {
    position: absolute;
    top: 5px;
    left: 50%;
    transform: translateX(-50%);
    margin-top: 12px;
    width: 125px;
    height: 125px;
    object-fit: cover;
    border-radius: 50%;
}

@keyframes moveDuck {
    from {
        transform: translateX(0);
    }
    to {
        transform: translateX(100vw);
    }
}

.duck_parent {
    position: relative;
    transform: translateX(0); 
}

.duck_parent.running {
    /* animation: moveDuck 10s linear forwards;  */
    animation: moveDuck linear forwards; 

}



</style>