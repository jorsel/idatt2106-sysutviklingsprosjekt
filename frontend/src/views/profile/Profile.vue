<template>
  <section class="box mainWrapper section">
    <div class="notification is-info" v-show="infoBox.show">
      <button @click="hideNotification" class="delete is-small" />
      {{ msg }}
    </div>

    <section class="section">
      <div class="card">
        <header class="card-header">
          <p class="card-header-title">Min profil</p>
        </header>
        <div class="card-image">
          <figure class="image is-5by3">
            <img :src="image" alt="profilePicture" />
          </figure>
        </div>
        <div class="card-content">
          <div class="media-content">
            <p class="title is-4">{{ firstName }} {{ lastName }}, {{ age }}</p>
            <p class="subtitle is-6">
              <span class="icon-text profileInfo">
                <div>
                  <span class="icon">
                    <i
                      class="fas fa-heartbeat"
                      @mouseover="heartIcon = true"
                      @mouseleave="heartIcon = false"
                    ></i>
                  </span>
                  <span v-if="heartIcon">Intensitetsnivå:</span>
                  {{ intensityText }}
                </div>
                <div v-show="publicCheck === 'false'">
                  <span class="icon">
                    <i class="fas fa-lock"></i>
                  </span>
                  Privat profil
                </div>
                <div v-show="publicCheck === 'true'">
                  <span class="icon">
                    <i class="fas fa-lock-open"></i>
                  </span>
                  Offentlig profil
                </div>
                <div>
                  <span class="icon">
                    <i
                      class="fas fa-star"
                      @mouseover="starIcon = true"
                      @mouseleave="starIcon = false"
                    ></i>
                  </span>
                  <span v-if="starIcon">Aktivitetspoeng:</span>
                  {{ points }}
                </div>
              </span>
            </p>
          </div>

          <div class="content">Elsker å være sosial!</div>
        </div>
        <footer class="card-footer">
          <a href="#" @click.prevent="toggleModal" class="card-footer-item">
            Bytt bilde
          </a>
          <router-link to="/profile/edit" class="card-footer-item">
            <a>Endre profil</a>
          </router-link>
        </footer>
      </div>
    </section>

    <div class="modal" :class="{ 'is-active': showModal }">
      <div class="modal-background"></div>
      <div class="modal-content">
        <ProfilePicture @update:picture="newPicture" />
      </div>
      <button
        @click="toggleModal"
        class="modal-close is-large"
        aria-label="close"
      ></button>
    </div>

    <section class="section">
      <h2 class="title is-4 is-spaced">Mine aktiviteter</h2>
      <div class="field is-grouped columns">
        <div class="control column">
          <Button class="" @click="AllSelect">Alle</Button>
          <Button class="" @click="MineSelect">Mine</Button>
          <Button class="" @click="PartSelect">Påmeldt</Button>
          <Button class="" @click="PreviousSelect">Gjennomført</Button>
        </div>
      </div>

      <article class="article" v-if="details">
        <Button @click="activityDetailsBack">Tilbake</Button>
        <ActivityDetails
          :id="selectedActivity.id"
          :title="selectedActivity.title"
          :description="selectedActivity.description"
          :location="selectedActivity.location"
          :start="selectedActivity.start"
          :end="selectedActivity.end"
          :equipment="selectedActivity.equipment"
          :weather="selectedActivity.weather"
          :participants="selectedActivity.participants"
          :maxAmount="selectedActivity.maxAmount"
          :waitinglist="selectedActivity.waitingList"
          :intensity="selectedActivity.intensity"
          :tags="selectedActivity.tags"
          :tagList="selectedActivity.tagList"
          :createdBy="selectedActivity.createdBy"
          :isEditable="false"
        />
        <ActivityJoin
          :selectedActivity="selectedActivity"
          :joinedActivities="joinedActivities"
          @update:participation="listenOnParticipation"
        />
      </article>

      <article v-else class="article">
        <ActivityPreview
          class="block"
          v-for="(activity, index) in activities"
          :key="index"
          @click="activityDetails(activity)"
          :title="activity.title"
          :description="activity.description"
          :location="activity.location"
          :start="activity.start"
          :end="activity.end"
          :equipment="activity.equipment"
          :weather="activity.weather"
          :participants="activity.participants"
          :maxAmount="activity.maxAmount"
          :waitinglist="activity.waitingList"
          :intensity="activity.intensity"
          :tags="activity.tags"
        />
      </article>
    </section>
  </section>
</template>

<script>
import ActivityPreview from "@/components/activity/ActivityPreview.vue";
import ActivityDetails from "@/components/activity/ActivityDetails.vue";
import ActivityJoin from "@/views/activity/ActivityJoin.vue";
import ProfilePicture from "@/components/ProfilePicture.vue";
import { reactive, ref, onMounted, computed, onUpdated } from "vue";
// import Label from "@/components/common/Label.vue";
import Button from "@/components/common/Button.vue";
// import Modal from "@/components/Modal.vue";
import { GET, baseURL, request } from "@/utility/request.js";

export default {
  name: "User Profile",
  components: {
    // Modal,
    ActivityPreview,
    ActivityDetails,
    ProfilePicture,
    ActivityJoin,
    // Label,
    Button,
  },
  props: {
    msg: {
      type: String,
      required: false,
    },
  },
  setup(props, { emit }) {
    const joinedActivities = ref([]);

    const heartIcon = ref(false);
    const starIcon = ref(false);

    const infoBox = reactive({
      show: false,
      msg: props.msg,
    });

    onUpdated(() => {
      if (props.msg) {
        emit("update:firstName");
      }
    });

    const showModal = ref(false);

    const toggleModal = () => {
      showModal.value = !showModal.value;
    };

    const myStorage = window.localStorage;

    const firstName = ref(myStorage.getItem("firstName"));
    const lastName = ref(myStorage.getItem("lastName"));
    const dateOfBirth = ref(myStorage.getItem("dateOfBirth"));
    const email = ref(myStorage.getItem("email"));
    const intensity = ref(myStorage.getItem("intensity"));
    const points = ref(myStorage.getItem("points"));
    const publicCheck = ref(myStorage.getItem("public"));
    const image = ref(myStorage.getItem("profilePic"));

    const intensityText = computed(() => {
      if (intensity.value === "2") {
        return "Medium";
      } else if (intensity.value === "3") {
        return "Høy";
      }
      return "Lav";
    });

    const activities = ref([]);

    const ms = new Date().getTime() - new Date(dateOfBirth.value).getTime();
    const msPerYear = 31557600000;
    let age = Math.floor(ms / msPerYear);

    if (age < 0) {
      age = 0;
    }

    const newPicture = () => {
      image.value = myStorage.getItem("profilePic");
      showModal.value = false;
      emit("update:thumbnail");
    };

    const details = ref(false);

    const hideNotification = () => {
      infoBox.show = false;
    };

    const AllSelect = async () => {
      activities.value = [];
      await getAct(`${baseURL}/profile/activities/created`, activities);
      await getAct(`${baseURL}/profile/activities/upcoming`, activities);
      removeDuplicate();
    };

    const MineSelect = async () => {
      activities.value = [];
      await getAct(`${baseURL}/profile/activities/created`, activities);
    };

    const PartSelect = async () => {
      activities.value = [];
      await getAct(`${baseURL}/profile/activities/upcoming`, activities);
    };
    const PreviousSelect = async () => {
      activities.value = [];
      await getAct(`${baseURL}/profile/activities/completed`, activities);
    };

    const selectedActivity = reactive({});
    const activityDetails = (a) => {
      details.value = true;

      Object.assign(selectedActivity, a);
    };

    const activityDetailsBack = () => {
      details.value = false;
    };

    const getTags = async () => {
      if (selectedActivity?.tagList?.length > 0) {
        return;
      }

      const response = await request(`${baseURL}/tags`, GET);

      if (response.status === 200) {
        selectedActivity.tagList = response.data;
      } else {
        console.error("An error occured while fetching activity tags!");
      }
    };

    const getAct = async (endpoint, arrayVar) => {
      if (window.localStorage.getItem("firstName") !== null) {
        await getTags();

        const response = await request(endpoint, GET);

        for (const a of response.data) {
          if (a.location === null) {
            a.location = {
              longitude: 1,
              latitude: 1,
            };
          }

          if (
            a.intensity === null ||
            a.intensity === undefined ||
            a.intensity === "undefined"
          ) {
            a.intensity = 1;
          }

          if (a.description === null) {
            a.description = "";
          }

          if (a.createdBy === null) {
            a.createdBy = {};
          }

          if (a.createdBy.firstName === null) {
            a.createdBy.firstName = "empty";
          }
          if (a.createdBy.lastName === null) {
            a.createdBy.lastName = "empty";
          }

          if (a.equipment === null) {
            a.equipment = [""];
          }

          a.start = new Date(a.start);
          a.end = new Date(a.end);

          arrayVar.value.push(a);
        }
      }
    };

    const hasSameId = (ele) => ele.id === selectedActivity.id;

    const replaceActivity = (actData) => {
      let indexAct = activities.value.findIndex(hasSameId);
      actData.start = new Date(actData.start);
      actData.end = new Date(actData.end);
      if (indexAct > -1) {
        activities.value.splice(indexAct, 1, actData);
      }
    };

    const removeDuplicate = () => {
      let dup = [];
      let act = [];
      activities.value.forEach((c) => {
        if (!dup.includes(c.id)) {
          dup.push(c.id);
          act.push(c);
        }
      });
      activities.value = act;
    };

    const listenOnParticipation = (el) => {
      replaceActivity(el);

      let indexJoined = joinedActivities.value.indexOf(el.id);

      if (indexJoined > -1) {
        joinedActivities.value.splice(indexJoined, 1);
      } else {
        joinedActivities.value.push(el.id);
      }
    };
    const getJoined = async () => {
      const response = await request(
        `${baseURL}/profile/activities/upcoming`,
        GET
      );

      for (const a of response.data) {
        joinedActivities.value.push(a.id);
      }
    };

    onMounted(async () => {
      await AllSelect();
      await getJoined();
      if (props.msg) {
        infoBox.show = true;
      }
    });

    return {
      activities,
      selectedActivity,
      activityDetailsBack,
      activityDetails,
      listenOnParticipation,
      joinedActivities,
      details,
      firstName,
      lastName,
      email,
      age,
      publicCheck,
      intensity,
      intensityText,
      points,
      hideNotification,
      infoBox,
      AllSelect,
      MineSelect,
      PartSelect,
      showModal,
      toggleModal,
      image,
      newPicture,
      PreviousSelect,
      heartIcon,
      starIcon,
    };
  },
};
</script>

<style scoped>
.profileInfo {
  flex-direction: column;
  margin: 1rem;
}
.mainWrapper {
  margin-left: auto;
  margin-right: auto;
}
</style>
