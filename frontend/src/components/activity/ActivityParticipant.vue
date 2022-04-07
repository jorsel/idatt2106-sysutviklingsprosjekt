<template>
  <article>
    <label class="label" v-if="label" for="participant">{{ label }}:</label>
    <p id="participant">{{ participantFirstname }} {{ participantLastname }}</p>
  </article>
</template>

<style scoped>
article {
  display: flex;
}
label {
  padding-right: 5px;
}
</style>

<script>
export default {
  name: "ActivityParticipant",
  props: {
    participantFirstname: {
      type: String,
      required: true,
    },
    participantLastname: {
      type: String,
      required: false,
    },
    label: {
      type: String,
      required: false,
    },
  },
  setup() {},
};
</script>
