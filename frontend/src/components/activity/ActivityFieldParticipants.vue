<template>
  <article class="editField">
    <label class="label" :for="id">{{ label }}:</label>
    <div :id="id" v-for="(participant, index) in content" :key="index">
      <ActivityParticipant
        :participantFirstname="participant.firstName"
        :participantLastname="participant.lastName"
      />
    </div>
  </article>
</template>

<style scoped>
article {
  display: flex;
}
p {
  padding: 0;
  margin: 0 4px;
}
</style>

<script>
import ActivityParticipant from "@/components/activity/ActivityParticipant.vue";
export default {
  name: "ActivityFieldParticipants",
  components: {
    ActivityParticipant,
  },
  props: {
    id: {
      type: String,
      required: true,
    },
    content: {
      type: Array,
      required: true,
    },
    label: {
      type: String,
      required: true,
    },
  },
};
</script>
