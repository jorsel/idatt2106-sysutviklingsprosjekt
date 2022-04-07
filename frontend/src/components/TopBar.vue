<template>
  <nav
    class="navbar is-fixed-top"
    role="navigation"
    aria-label="main navigation"
  >
    <div class="navbar-brand">
      <a class="navbar-item" href="/">
        <img alt="Gidd logo" id="logo" src="../assets/1.png" />
      </a>
      <a
        role="button"
        class="navbar-burger"
        :class="{ 'is-active': mobileMenu }"
        aria-label="menu"
        aria-expanded="false"
        data-target="navbarBasicExample"
        @click="mobileMenu = !mobileMenu"
      >
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>

    <div
      id="navbarBasicExample"
      class="navbar-menu"
      :class="{ mobile: mobileMenu }"
    >
      <div class="navbar-start">
        <a
          class="navbar-item"
          :class="{ 'is-active': currentPath === '/activities' }"
          @click="routeTo('/activities')"
        >
          Aktiviteter
        </a>
        <a
          class="navbar-item"
          :class="{ 'is-active': currentPath === '/calendar' }"
          @click="routeTo('/calendar')"
        >
          Kalender
        </a>
        <a
          class="navbar-item"
          :class="{ 'is-active': currentPath === '/leaderboard' }"
          @click="routeTo('/leaderboard')"
        >
          Poengtabell
        </a>
        <a
          class="navbar-item"
          :class="{ 'is-active': currentPath === '/feedback' }"
          @click="routeTo('/feedback')"
        >
          Tilbakemelding
        </a>
        <a
          class="navbar-item"
          :class="{ 'is-active': currentPath === '/profile' }"
          v-if="loggedIn && mobileMenu"
          @click="routeTo('/profile')"
        >
          Profil
        </a>
        <a
          class="navbar-item"
          :class="{ 'is-active': currentPath === '/register' }"
          v-if="!loggedIn"
          @click="routeTo('/register')"
        >
          Registrer
        </a>
        <a
          class="navbar-item"
          :class="{ 'is-active': currentPath === '/login' }"
          v-if="!loggedIn && mobileMenu"
          @click="routeTo('/login')"
        >
          Logg inn
        </a>
        <a class="navbar-item" v-if="loggedIn && mobileMenu" @click="logOutFunc"
          >Logg ut</a
        >
      </div>

      <div class="navbar-end" v-if="!mobileMenu">
        <div class="navbar-item has-dropdown is-hoverable test">
          <article class="profile" v-if="!loggedIn">
            <router-link to="/login"
              ><p class="login-text">Logg inn</p></router-link
            >
            <i class="fas fa-user fa-3x fa-user-not"></i>
          </article>
          <article class="profile" v-if="loggedIn">
            <p class="first-name">{{ firstName }}</p>
            <router-link to="/profile">
              <figure class="image is-60x60">
                <img class="is-rounded fa-user-is" :src="thumbnail" />
              </figure>
              <!--<i class="fas fa-user fa-3x fa-user-is"></i>-->
            </router-link>
          </article>
          <div class="navbar-dropdown is-right" v-if="loggedIn">
            <router-link to="/profile" class="navbar-item">
              Profil
            </router-link>
            <a class="navbar-item" @click="logOutFunc"> Logg ut </a>
          </div>
        </div>

        <div class="navbar-item has-dropdown is-hoverable test">
          <i
            v-if="loggedIn"
            id="bellicon"
            class="fas fa-bell fa-3x fa-stack-3x navbar-link is-arrowless bell"
          >
            <span class="bellnumber" v-if="alerts.length > 0">{{
              alerts.length
            }}</span>
          </i>

          <div class="navbar-dropdown is-right" v-if="alerts.length > 0">
            <AlertBar
              :alerts="alerts"
              @update:seen="$emit('update:seen', $event)"
            />
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import AlertBar from "@/components/AlertBar.vue";
import { defineComponent, computed, ref, onUpdated } from "vue";
import { useRouter } from "vue-router";
import { GET, POST, baseURL, request } from "@/utility/request.js";

export default defineComponent({
  name: "TopBar",
  props: {
    loggedIn: {
      type: Boolean,
      required: true,
    },
    alerts: {
      type: Array,
      required: true,
    },
    updatedThumbnail: {
      type: String,
      required: true,
    },
  },
  components: {
    AlertBar,
  },
  setup(props, { emit }) {
    const router = useRouter();
    const mobileMenu = ref(false);

    const firstName = ref("");
    const thumbnail = ref("");

    onUpdated(() => {
      if (props.loggedIn) {
        firstName.value = window.localStorage.getItem("firstName");
        thumbnail.value = window.localStorage.getItem("profilePicThumbnail");
      }
    });

    const currentPath = computed(() => {
      return router.currentRoute.value.path;
    });

    const alertsClicked = async () => {
      const response = await request(`${baseURL}/profile/notifications`, GET);
      const data = response.data;
      emit("alertsClicked", data);
    };

    const routeTo = (path) => {
      mobileMenu.value = false;
      router.push({ path: path });
    };

    const logOutFunc = async () => {
      const response = await request(`${baseURL}/logout`, POST, {});

      if (response.status === 200) {
        window.localStorage.clear();
        mobileMenu.value = false;
        emit("update:logout");
        router.push({ path: "/login" });
      } else {
        console.error("An errror occured when trying to logout!");
      }
    };

    return {
      logOutFunc,
      routeTo,
      currentPath,
      firstName,
      alertsClicked,
      mobileMenu,
      thumbnail,
    };
  },
});
</script>

<style scoped>
.bell {
  color: black;
}

.bell:hover {
  color: grey;
}

.bell .bellnumber {
  position: absolute;
  border-radius: 50%;
  background-color: #ed1c24;
  color: white;
  top: 15px;
  right: 12px;
  width: 18px;
  height: 18px;
  justify-content: center;
  align-items: center;
  display: flex;
  font-size: 1.25rem;
  padding-bottom: 3px;
  font-weight: 550;
}

.test {
  margin-right: 15px;
  text-align: center;
}

.mobile {
  display: block;
}

.profile {
  max-width: 250px;
  display: flex;
  align-items: center;
}

.login-text,
.first-name {
  padding-right: 5px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.fa-user-is {
  color: #ed1c24;
}

img {
  max-height: 4.5rem;
}

.is-60x60 {
  height: 60px;
  width: 60px;
}
</style>
