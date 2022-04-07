<template>
  <div class="positionfix">
    <Alert
      v-for="(alert, index) in alerts"
      :key="index"
      @alertClicked="alertClicked"
      :alert="alert"
    />
  </div>
</template>

<script>
import Alert from "@/components/Alert.vue";

export default {
  name: "AlertBar",

  components: { Alert },

  props: {
    alerts: {
      type: Array,
      required: true,
    },
  },

  methods: {
    alertClicked(alert) {
      this.$emit("update:seen", alert);
    },
  },
};
</script>

<style scoped>
.positionfix {
  width: 265px;
}
</style>
