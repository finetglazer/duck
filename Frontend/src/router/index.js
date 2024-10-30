import { createRouter, createWebHistory } from 'vue-router'
import Home from '../components/Home.vue'
import Team from '../components/Team/Team.vue'
import Zoom from '../components/Zoom/Zoom.vue'
import Progress from '../components/Progress/Progress.vue'
import Project from '../components/Project/Project.vue'
import ZoomDuck from '../components/Zoom/ZoomDuck.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/team',
    name: 'Team',
    component: Team
  },
  {
    path: '/zoom',
    name: 'Zoom',
    component: Zoom
  },
  {
    path: '/project',
    name: 'Project',
    component: Project
  },
  {
    path: '/progress',
    name: 'Progress',
    component: Progress
  },
  {
    path: '/zoom/zoom-duck',
    name: 'ZoomDuck',
    component: ZoomDuck
  }
]

const router = createRouter({
  history: createWebHistory("/"),
  routes
})

export default router
