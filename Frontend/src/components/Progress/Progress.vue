<template>
    <div class="progress">
        <div class="card-body">
                <h1 class="card-title title_zoom text-primary mb-3">
                    Progress
                </h1>
                <div class="card-text d-flex gap-5">
                    <span class="text-danger" style="font-weight: 600;">Frontend: </span>
                    <ul>
                        <li>Home Page</li>
                        <li>
                            Zoom Page
                        </li>
                        
                    </ul>
                </div>
                <div class="card-text d-flex gap-5">
                    <span class="text-warning" style="font-weight: 600;">Backend: </span>
                    <ul>
                        <li>Create Project</li>
                        <li>
                            ...
                        </li>
                        
                    </ul>
                </div>
                <!-- <a href="#" class="btn btn-warning">Join zoom</a> -->
            </div>
    </div>
</template>

<script setup>

</script>

<style lang="css" scoped>
.progress {
    height: calc(100vh - 80px);
    padding: 50px 200px;
}
</style>