<template>
  <section class="section less-padding">
    <TopBar
      :loggedIn="isLoggedIn"
      @update:logout="checkLogoutStatus"
      :alerts="alerts"
      @update:seen="alertSeen"
      :updatedFirstName="firstName"
      :updatedThumbnail="thumbnail"
    />
  </section>
  <main>
    <router-view
      @update:login="checkLoginStatus"
      @update:firstName="updateFirstName"
      @update:thumbnail="updateThumbnail"
    />
  </main>
</template>

<script>
import TopBar from "@/components/TopBar.vue";
import { defineComponent } from "vue";
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { GET, baseURL, request } from "@/utility/request.js";

export default defineComponent({
  name: "App",

  components: {
    TopBar,
  },

  data() {
    return {
      alerts: [],
      isLoggedIn: false,
      firstName: "",
      thumbnail: "",
    };
  },

  methods: {
    alertSeen(alert) {
      this.alerts = this.alerts.filter((item) => item.id !== alert.id);
    },
    connect() {
      this.socket = new SockJS("http://localhost:8080/websocket");
      this.stompClient = Stomp.over(this.socket);
      this.stompClient.debug = () => {}
      this.stompClient.connect(
        {},
        () => {
          this.connected = true;
          this.stompClient.subscribe("/user/queue/notifications", (tick) => {
            this.alerts.unshift(JSON.parse(tick.body));
          });
        },
        () => {
          this.connected = false;
        }
      );
    },
    disconnect() {
      if (this.stompClient) {
        this.stompClient.disconnect();
      }
      this.connected = false;
    },
    async checkLoginStatus() {
      if (window.localStorage.getItem("firstName")) {
        this.isLoggedIn = true;
        if (!this.connected) {
          const response = await request(
            `${baseURL}/profile/notifications`,
            GET
          );
          this.alerts = response.data;
          this.connect();
        }
      }
    },
    checkLogoutStatus() {
      this.alerts = [];
      this.isLoggedIn = false;
    },
    updateFirstName() {
      this.firstName = window.localStorage.getItem("firstName");
    },
    updateThumbnail() {
      this.thumbnail = window.localStorage.getItem("profilePicThumbnail");
    },
  },

  async mounted() {
    let myStorage = window.localStorage;
    if (myStorage.getItem("firstName")) {
      this.isLoggedIn = true;
      if (!this.connected) {
        const response = await request(`${baseURL}/profile/notifications`, GET);
        this.alerts = response.data;
        this.connect();
      }
    }
  },
});
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#wrapper {
  background-color: #fbfbfb;
  display: flex;
  flex-direction: column;
}

main {
  background-color: #fbfbfb;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 10px;
}
</style>
<style>
body {
  font-family: Arial, Helvetica, sans-serif;
}

.colorbar {
  padding: 5px;
  top: 100%;
  left: 100%;
  background-color: #ed1c24;
  /*background-position: 0% 25%; */
  width: 100%;
  background-repeat: no-repeat;
  border: 2px;
}

.editField {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 5px 0 5px 0;
}

.less-padding {
  padding-bottom: 0;
  padding-top: 2.1rem;
}
</style>
