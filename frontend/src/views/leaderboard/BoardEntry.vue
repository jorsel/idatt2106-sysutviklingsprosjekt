<template>
  <td :class="{ you: isYou }">{{ name }}</td>
  <td :class="{ you: isYou }">{{ getIntensity }}</td>
  <td :class="{ you: isYou }">{{ points }}</td>
  <td :class="{ you: isYou }">{{ position }}</td>
</template>

<style scoped>
.you {
  background-color: #ed1c24;
  color: white;
}
</style>

<script>
export default {
  name: "BoardEntry",

  props: {
    name: {
      type: String,
      required: true,
    },
    intensity: {
      type: Number,
      required: true,
    },
    points: {
      type: Number,
      required: true,
    },
    position: {
      type: Number,
      required: true,
    },
    isYou: {
      type: Boolean,
      required: true,
    },
  },

  computed: {
    getIntensity(props) {
      switch (props.intensity) {
        case 1:
          return "Lav";
        case 2:
          return "Medium";
        case 3:
          return "Høy";
        default:
          return "-";
      }
    },
  },
};
</script>
