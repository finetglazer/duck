<template>
    <div class="zoom_duck">
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
            <div v-for="(duck, index) in ducks" :key="index" class="duck_parent" :style="{ animationDuration: duck.duration + 's' }" :class="{ running: ducksRunning }">
                <img :src="duck.image" class="card-img-top icon_race" alt="...">
                <span class="duck_num">{{ index + 1 }}</span>
            </div>
        </div>

        <!-- <div class="d-flex flex-column duck_block">
            <div class="duck_parent" :class="{ running: ducksRunning }">
                <img :src="duck" class="card-img-top icon_race" alt="..."> 
                <span class="duck_num">1</span>
            </div>
    
            <div class="duck_parent" :class="{ running: ducksRunning }">
                <img :src="duck" class="card-img-top icon_race" alt="..."> 
                <span class="duck_num">2</span>
            </div>

            <div class="duck_parent" :class="{ running: ducksRunning }">
                <img :src="duck" class="card-img-top icon_race" alt="..."> 
                <span class="duck_num">3</span>
            </div>

            <div class="duck_parent" :class="{ running: ducksRunning }">
                <img :src="duck" class="card-img-top icon_race" alt="..."> 
                <span class="duck_num">4</span>
            </div>

            <div class="duck_parent" :class="{ running: ducksRunning }">
                <img :src="duck" class="card-img-top icon_race" alt="..."> 
                <span class="duck_num">5</span>
            </div>
        </div> -->
    </div>

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
import dollar from "../../assets/img/dollar.png"
import wallet from "../../assets/img/wallet.png"
import progress from "../../assets/img/game_progress_white_bg.png"
import loss from "../../assets/img/loss.png"
import win from "../../assets/img/win.png"
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
const ducks = ref(
  Array.from({ length: 5 }, () => ({
    image: duck,
    duration: Math.random() * 5 + 10, 
  }))
);
const socket = new WebSocket(`wss://pure-caverns-67534-35c6a327ed88.herokuapp.com/duckRace/race?playerId=${player_id}`);
socket.addEventListener('open', function (event) {
    console.log('Connected to WebSocket server duckRace/race');
});

socket.addEventListener('message', function (event) {
    console.log(event.data);
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
        const player =  JSON.parse(event.data).players.find(player => player.id === player_id);
        if (player) {
            point.value = player.points;
        }e
    } else if(JSON.parse(event.data).type === "error") {
        toast.error("Player banned!Please Join zoom again!!!");
        setTimeout(() => {
            router.push("/zoom");
        }, 3000);
    } else {
        console.log("no non nonono");
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

.zoom_duck {
    position: relative;
    width: calc(100vw - 10px);
    height: calc(100vh - 100px);
    object-fit: cover;
    background-image: url("../../assets/img/riverriver.png");
    background-repeat: no-repeat;
    background-size: cover; 
    background-position: center; 
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

/* .duck_parent:nth-child(1) {
    animation-delay: 2s;
}

.duck_parent:nth-child(2) {
    animation-delay: 1.5s;
}

.duck_parent:nth-child(3) {
    animation-delay: 1s;
}

.duck_parent:nth-child(4) {
    animation-delay: 2.5s;
}

.duck_parent:nth-child(5) {
    animation-delay: 3s;
} */


</style>