import { createApp } from "vue";
import VCalendar from "v-calendar";
import App from "./App.vue";
import router from "./router";

createApp(App).use(router).use(VCalendar).mount("#app");
