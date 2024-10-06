<template>
    <div class="d-flex gap-3">
        <div class="zoom">
            <div class="card" style="">
                <div class="card-body">
                    <h5 class="card-title title_zoom text-primary">
                        RACE DUCK
                        <img :src="duck" class="card-img-top icon_race" alt="...">
                    </h5>
                    <p class="card-text"> Player bet on the duck you want to choose</p>
                    <a href="#" class="btn btn-primary">Join zoom</a>
                </div>
            </div>
            <div class="card" style="margin-top: 20px">
                <div class="card-body">
                    <h5 class="card-title title_zoom text-warning">
                        DOG RACE
                        <img :src="dog" class="card-img-top icon_race" alt="...">
                    </h5>
                    <p class="card-text">Player bet on the dog you want to choose</p>
                    <a href="#" class="btn btn-warning">Join zoom</a>
                </div>
            </div>
            <div class="card" style="margin-top: 20px">
                <div class="card-body">
                    <h5 class="card-title title_zoom text-danger">
                        TURTLE RACE
                        <img :src="turtle" class="card-img-top icon_race" alt="...">
                    </h5>
                    <p class="card-text">Player bet on the turtle you want to choose</p>
                    <a href="#" class="btn btn-danger">Join zoom</a>
                </div>
            </div>
            <div class="card" style="margin-top: 20px">
                <div class="card-body">
                    <h5 class="card-title title_zoom text-success">
                        CAMEL RACE
                        <img :src="camel" class="card-img-top icon_race" alt="...">
                    </h5>
                    <p class="card-text">Player bet on the camel you want to choose</p>
                    <a href="#" class="btn btn-success">Join zoom</a>
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
                    <p class="card-text d-flex gap-5" v-for="(user, index) in users" :key="index">
                        <span class="text-warning" style="font-weight: 600;">
                            <span class="text-danger">#{{ index + 1}}.</span>
                            {{ user.name }} 
                        </span>
                        <!-- <span class="text-danger">{{ user.coin }}</span> -->
                        <span class="badge rounded-pill text-bg-info text-white">{{ user.coin }}</span>
                    </p>
                    <!-- <a href="#" class="btn btn-warning">Join zoom</a> -->
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue"
import duck from "../../assets/img/duck-toy.png"
import turtle from "../../assets/img/turtle.png"
import camel from "../../assets/img/camel _2.png"
import dog from "../../assets/img/dog.png"
import rank from "../../assets/img/ranking.png"

const users = ref([
    {
        name: "Player1",
        coin: "2000"
    },
    {
        name: "Player2",
        coin: "1500"
    },
    {
        name: "Player3",
        coin: "1200"
    },
])
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
</style>