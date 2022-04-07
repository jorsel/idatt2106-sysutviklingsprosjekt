<template>
  <div v-if="joinable">
    <div v-if="waitable">
      <Button @click="joinActivity">Meld deg på</Button>
    </div>
    <div v-else>
      <Button @click="joinActivity">Meld deg på venteliste</Button>
    </div>
  </div>
  <div v-else>
    <Button @click="leaveActivity">Meld deg av</Button>
  </div>
</template>

<script>
import { PUT, baseURL, request } from "@/utility/request.js";
import Button from "@/components/common/Button";
import { ref, onMounted } from "vue";

export default {
  name: "ActivitieCreateView",
  components: {
    Button,
  },
  props: {
    selectedActivity: {
      type: Object,
      required: true,
    },
    joinedActivities: {
      type: Array,
      required: true,
    },
  },
  setup(props, { emit }) {
    const joinable = ref(true);
    const waitable = ref(true);

    const toggleJoinable = () => {
      joinable.value = !joinable.value;
    };

    const joinActivity = async () => {
      const response = await request(
        `${baseURL}/activity/${props.selectedActivity.id}/join`,
        PUT
      );

      if (response.status === 200) {
        toggleJoinable();
        emit("update:participation", response.data);
      } else {
        console.error("an error happened while joining activity");
      }
    };

    const leaveActivity = async () => {
      const response = await request(
        `${baseURL}/activity/${props.selectedActivity.id}/leave`,
        PUT
      );

      if (response.status === 200) {
        toggleJoinable();
        emit("update:participation", response.data);
      } else {
        console.error("an error happened while leaving activity");
      }
    };

    onMounted(() => {
      joinable.value = !props.joinedActivities.includes(
        props.selectedActivity.id
      );
      waitable.value =
        props.selectedActivity.participants.length <
          props.selectedActivity.maxAmount || !joinable.value;
    });

    return { joinable, waitable, joinActivity, leaveActivity };
  },
};
</script>
