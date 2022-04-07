<template>
  <section id="create">
    <div class="notification is-danger" v-show="msg">
      {{ msg }}
    </div>
    <ActivityCreate :tagList="tagList" @update:create="listenOnCreate" />
  </section>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import ActivityCreate from "@/components/activity/ActivityCreate.vue";
import { POST, GET, baseURL, request } from "@/utility/request.js";

export default {
  name: "ActivitieCreateView",
  components: {
    ActivityCreate,
  },
  setup() {
    const router = useRouter();

    // let tempActivity = {}
    let tagList = ref([]);
    const msg = ref();

    const getTags = async () => {
      const response = await request(`${baseURL}/tags`, GET);

      if (response.status === 200) {
        for (const tag of response.data) {
          tagList.value.push(tag);
        }
      } else {
        console.error("An error occured while fetching activity tags!");
      }
    };

    onMounted(async () => {
      await getTags();
    });

    const listenOnCreate = (obj) => {
      let tempStart = new Date(obj.start);
      let tempEnd = new Date(obj.end);
      if (
        obj.title === null ||
        obj.title === "" ||
        obj.description === null ||
        obj.description === "" ||
        obj.location.address === "" ||
        obj.start === null ||
        obj.end === null
      ) {
        msg.value = "Fyll inn nødvendig informasjon!";
        return;
      } else if (2 > obj.maxAmount || obj.maxAmount > 50 || isNaN(obj.maxAmount)) {
        msg.value = "Maks påmeldte skal være mellom 2 og 50";
        return;
      } else if (
        Date.now() >= tempStart ||
        tempStart >= tempEnd ||
        tempEnd.getTime() - tempStart.getTime() > 604800000
      ) {
        msg.value = "Ugyldig dato (makslengde på aktivitet er én uke)";
        return;
      } else {
        createNewActivity(obj);
      }
    };

    const createNewActivity = async (tempActivity) => {
      const response = await request(
        `${baseURL}/activity`,
        POST,
        JSON.stringify(tempActivity)
      );

      if (response.status === 200) {
        router.push({
          name: "Activities",
          params: { msgActCreate: "Aktivitet opprettet" },
        });
      } else {
        msg.value = "En feil har oppstått, aktivitet ikke opprettet";
      }
    };

    return { listenOnCreate, tagList, msg };
  },
};
</script>

<style scoped>
#create {
  padding: 10px;
  margin-left: auto;
  margin-right: auto;
  display: flex;
  justify-content: center;
}
#create * {
  margin-left: auto;
  margin-right: auto;
  position: center;
}
</style>
