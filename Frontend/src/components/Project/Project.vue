<template>
    <div class="project">
        <div class="project_content">
            <div class="card-body">
                <h3 class="card-title title_zoom text-primary mb-3">
                    Title: Website for Animal Races
                </h3>
                <p class="card-text d-flex gap-5">
                    <span class="text-danger" style="font-weight: 600;">Description: </span>
                    With the desire to create a healthy betting platform for enthusiasts of gambling related to races, our team aims to build a website offering a wide variety of race models to satisfy players' passion. The website will serve as a hub, and anyone who connects to this address will be able to participate in the games.
                </p>
                <div class="card-text">
                    <span class="text-success" style="font-weight: 600;">Technology: </span>
                    <ul>
                        <li>Frontend: VueJS + Bootstrap</li>
                        <li>
                            Backend: Java(use UDP)
                        </li>
                        
                    </ul>
                </div>
                <div>
                    <span class="text-info" style="font-weight: 600;">Project Scope: </span>
                    <ul>
                        <li>No support for money transactions</li>
                        <li>
                            No 24/7 messaging service support
                        </li>
                        <li>Does not promote gambling</li>
                        <li>For players aged 18 and above</li>
                    </ul>
                </div>
                <!-- <a href="#" class="btn btn-warning">Join zoom</a> -->
            </div>
        </div>
    </div>
</template>

<script setup>

</script>

<style lang="css" scoped>
.project {
    margin-top: 50px;
    height: calc(100vh - 80px);
}

.project_content {
    width: 60%;
    margin: 0 auto;
}
</style>