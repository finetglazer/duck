<template>
<div class="container team-section text-center py-5">
    <h2 class="team-title mb-4">Team E21PM4</h2>
    <p class="team-description mb-5">
        We are brothers, capable of doing anything. We will not back down and always move forward.
    </p>
    <div class="row">
    <!-- Team Member 1 -->
    <div class="col-md-4">
        <div class="team-member">
        <img :src="hungImg" alt="Dr. Luke Bucci" class="rounded-circle mb-3" />
        <h5>Trần Mạnh Hùng</h5>
        <p>B21DCCN419</p>
        </div>
    </div>
    
    <!-- Team Member 2 -->
    <div class="col-md-4">
        <div class="team-member">
        <img :src="chinhImg" alt="Dr. Nima Alamdari" class="rounded-circle mb-3" />
        <h5>Đoàn Duy Chinh</h5>
        <p>B21DCVT084</p>
        </div>
    </div>
    
    <!-- Team Member 3 -->
    <div class="col-md-4">
        <div class="team-member">
        <img :src="hiepImg" alt="Dr. Mastaneh Sharafi" class="rounded-circle mb-3" />
        <h5>Nguyễn Vĩnh Hiệp</h5>
        <p>B21DCAT084</p>
        </div>
    </div>
    </div>
</div>
</template>

<script setup>
import chinhImg from "../../assets/img/chinh.jpeg"
import hungImg from "../../assets/img/hung.jpg"
import hiepImg from "../../assets/img/hiep_n.jpg"
</script>

<style lang="scss" scoped>
.team-section {
    height: calc(100vh - 80px);
}

.team-title {
font-size: 2rem;
font-weight: bold;
}

.team-description {
color: #6c757d;
font-size: 1rem;
}

.team-member img {
width: 150px;
height: 150px;
object-fit: cover;
}

.team-member h5 {
font-size: 1.25rem;
margin-top: 10px;
}

.team-member p {
font-size: 0.9rem;
color: #6c757d;
}
</style>
