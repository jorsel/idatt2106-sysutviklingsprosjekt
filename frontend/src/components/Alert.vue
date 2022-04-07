<template>
  <article class="message is-danger">
    <div class="message-header">
      <p>Notifikasjon</p>
      <button class="delete" aria-label="delete" @click="alertClicked"></button>
    </div>
    <div class="message-body">
      {{ alert.message }}
    </div>
  </article>
  <!-- <div @click="alertClicked">
    <p id="message">{{ alert.message }}</p>
  </div> -->
</template>

<script>
import { PUT, baseURL, request } from "@/utility/request.js";

export default {
  name: "Notification",

  emits: ["alertClicked"],

  props: {
    alert: {
      required: true,
    },
  },

  methods: {
    async alertClicked() {
      await request(
        `${baseURL}/profile/notifications`,
        PUT,
        JSON.stringify(this.alert)
      );
      this.$emit("alertClicked", this.alert);
    },
  },
};
</script>

<style scoped>
</style>
