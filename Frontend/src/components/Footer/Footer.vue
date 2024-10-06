<template>
    <footer>
      <ul class="social-icons">
        <li><a href="https://www.facebook.com/doanchinhit2102" target="_blank"><i class="bi bi-facebook text-primary"></i></a></li>
        <li><a href="#" target="_blank"><i class="bi bi-instagram text-danger"></i></a></li>
        <li><a href="#" target="_blank"><i class="bi bi-twitter-x text-info"></i></a></li>
        <li><a href="#" target="_blank"><i class="bi bi-github"></i></a></li>
        <li><a href="#" target="_blank"><i class="bi bi-youtube text-danger"></i></a></li>
      </ul>
      <ul class="footer-nav">
        <li class="nav-item">
            <router-link class="nav-link text-white" to="/">Home</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link text-white" to="/team">Team</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link text-white" to="/project">
              Project
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link text-white" to="/progress">Progress</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link text-white" to="/zoom">Zoom</router-link>
          </li>
      </ul>
      <p class="footer-text">Copyright ©2024, Designed by Duy Chinh</p>
    </footer>
  </template>
  
  <script setup>
  // No special scripts are required for this example
  </script>
  
  <style lang="scss" scoped>
  footer {
    background-color: #111;
    color: #fff;
    text-align: center;
    padding: 20px 0;
    position: relative;
    width: 100%;
  }
  
  .social-icons {
    display: flex;
    justify-content: center;
    list-style: none;
    padding: 10px 0;
  
    li {
      margin: 0 10px;
  
      a {
        display: inline-block;
        width: 40px;
        height: 40px;
        background-color: #fff;
        color: #111;
        border-radius: 50%;
        text-align: center;
        line-height: 40px;
        font-size: 20px;
        transition: all 0.3s;
  
        &:hover {
          background-color: #888;
          color: #fff;
        }
      }
    }
  }
  
  .footer-nav {
    display: flex;
    justify-content: center;
    list-style: none;
    padding: 10px 0;
  
    li {
      margin: 0 15px;
  
      a {
        color: #fff;
        text-decoration: none;
        transition: color 0.3s;
  
        &:hover {
          color: #888;
        }
      }
    }
  }
  
  .footer-text {
    padding-top: 10px;
    font-size: 14px;
    color: #888;
  }
  </style>
  