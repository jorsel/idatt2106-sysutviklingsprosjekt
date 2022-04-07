import { createRouter, createWebHistory } from "vue-router";

import Activities from "@/views/activity/Activities.vue";
import Profile from "@/views/profile/Profile.vue";
import ProfileEdit from "@/views/profile/ProfileEdit.vue";
import Register from "@/views/register/Register.vue";
import ActivityCreateView from "@/views/activity/ActivityCreateView.vue";
import Login from "@/views/login/Login.vue";
import Feedback from "@/views/feedback/Feedback.vue";
import Calendar from "@/views/profile/Calendar.vue";
import Leaderboard from "@/views/leaderboard/Leaderboard.vue";
import ActivityJoin from "@/views/activity/ActivityJoin";
const routes = [
  {
    path: "/activities",
    name: "Activities",
    component: Activities,
    props: true,
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
    props: true,
  },
  {
    path: "/profile/edit",
    name: "ProfileEdit",
    component: ProfileEdit,
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
    props: true,
  },
  {
    path: "/activity/create",
    name: "ActivityCreateView",
    component: ActivityCreateView,
  },
  {
    path: "/Feedback",
    name: "Feedback",
    component: Feedback,
  },
  {
    path: "/calendar",
    name: "Calendar",
    component: Calendar,
  },
  {
    path: "/leaderboard",
    name: "Leaderboard",
    component: Leaderboard,
  },
  {
    path: "/activity/joinactivity",
    name: "ActivityJoin",
    component: ActivityJoin,
  },

  {
    path: "/",
    redirect: { name: "Activities" },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
