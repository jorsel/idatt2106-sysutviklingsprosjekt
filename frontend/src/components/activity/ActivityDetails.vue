<template>
  <div class="box">
    <section>
      <div class="notification is-danger" v-show="msg">
        {{ msg }}
      </div>
      <fieldset>
        <h1 class="title is-4">{{ title }}</h1>
        <ActivityParticipant
          :id="'createdby'"
          :participantFirstname="createdBy.firstName"
          :participantLastname="createdBy.lastName"
          :label="'Laget av'"
        />
        <ActivityFieldTextArea
          :id="'description'"
          :content="description"
          :label="'Beskrivelse'"
          :edit="enableEdit"
          @update:description="listenOnValueChange"
        />
        <ActivityFieldLocation
          :id="'location'"
          :content="location"
          :label="'Adresse'"
          :edit="enableEdit"
          @update:location="listenOnValueChange"
        />

        <div v-show="!enableEdit">
          <div class="modal" :class="{ 'is-active': mapPop }">
            <div class="modal-background"></div>
            <div class="modal-content">
              <ActivityMap :location="location" />
            </div>
            <button
              class="modal-close is-large"
              aria-label="close"
              @click="mapPop = !mapPop"
            ></button>
          </div>
          <Button @click="mapPop = !mapPop">Vis på kart</Button>
        </div>

        <ActivityFieldDate
          :id="'start'"
          :content="start"
          :label="'Start-tidspunkt'"
          :edit="enableEdit"
          @update:start="listenOnValueChange"
        />
        <ActivityFieldDate
          :id="'end'"
          :content="end"
          :label="'Slutt-tidspunkt'"
          :edit="enableEdit"
          @update:end="listenOnValueChange"
        />
        <ActivityFieldLevelOptions
          :id="'intensity'"
          :content="intensity"
          :label="'Aktivitetsnivå'"
          :edit="enableEdit"
          @update:intensity="listenOnValueChange"
        />
        <ActivityFieldEquipment
          :id="'equipment'"
          :content="equipment"
          :label="'Ting som trengs'"
          :edit="enableEdit"
          @update:equipment="listenOnValueChange"
          @update:removeEquipment="listenOnRemoveEquipment"
        />
        <ActivityFieldParticipants
          :id="'participants'"
          :content="participants"
          :label="'Påmeldte'"
        />
        <ActivityFieldParticipants
          :id="'waitinglist'"
          :content="waitinglist"
          :label="'Venteliste'"
        />
        <ActivityFieldTags
          :id="'tags'"
          :content="tags"
          :tagList="tagList"
          :label="'Tags'"
          :edit="enableEdit"
          @update:tags="listenOnValueChange"
          @update:removeTag="listenOnRemoveTag"
        />
      </fieldset>

      <!-- See edit and delete if activity belong to you -->
      <div v-show="isCreator && isEditable">
        <div v-if="enableEdit">
          <Button @click="saveActivity">Lagre</Button>
          <Button @click="toggleEdit">Avbryt</Button>
        </div>
        <div v-else>
          <Button @click="toggleEdit">Rediger aktivitet</Button>
          <Button @click="togglePop">Slett aktivitet</Button>

          <Confirmation
            v-show="pop"
            :confirmation="deleteActivity"
            :goBack="togglePop"
          >
            <h3>Er du sikker på at du vil slette aktiviteten?</h3>
          </Confirmation>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import ActivityFieldLocation from "@/components/activity/ActivityFieldLocation.vue";
import ActivityFieldDate from "@/components/activity/ActivityFieldDate.vue";
import ActivityMap from "@/components/activity/ActivityMap.vue";
import ActivityFieldEquipment from "@/components/activity/ActivityFieldEquipment.vue";
import ActivityFieldTags from "@/components/activity/ActivityFieldTags.vue";
import ActivityFieldLevelOptions from "@/components/activity/ActivityFieldLevelOptions.vue";
import ActivityFieldParticipants from "@/components/activity/ActivityFieldParticipants.vue";
import ActivityFieldTextArea from "@/components/activity/ActivityFieldTextArea.vue";
import ActivityParticipant from "@/components/activity/ActivityParticipant.vue";
import Confirmation from "@/components/Confirmation";
import Button from "@/components/common/Button";

export default {
  name: "ActivityPreview",
  components: {
    Confirmation,
    ActivityMap,
    ActivityFieldDate,
    ActivityFieldEquipment,
    ActivityFieldTags,
    ActivityFieldLevelOptions,
    ActivityFieldParticipants,
    ActivityParticipant,
    ActivityFieldLocation,
    ActivityFieldTextArea,
    Button,
  },
  props: {
    id: {
      type: Number,
      required: true,
    },
    title: {
      type: String,
      required: true,
    },
    description: {
      type: String,
      required: true,
    },
    location: {
      type: Object,
      required: true,
    },
    start: {
      type: Date,
      required: true,
    },
    end: {
      type: Date,
      required: true,
    },
    equipment: {
      type: Array,
      required: false,
    },
    weather: {
      type: String,
      required: false,
    },
    intensity: {
      type: Number,
      required: true,
    },
    participants: {
      type: Array,
      required: true,
    },
    maxAmount: {
      type: Number,
      required: true,
    },
    waitinglist: {
      type: Array,
      required: true,
    },
    tags: {
      type: Array,
      required: false,
    },
    tagList: {
      type: Array,
      required: false,
    },
    createdBy: {
      type: Object,
      required: true,
    },
    isEditable: {
      type: Boolean,
      required: false,
      default: true,
    },
  },
  setup(props, { emit }) {
    const msg = ref("");
    const mapPop = ref(false);
    const pop = ref(false);

    let togglePop = () => {
      pop.value = !pop.value;
    };

    const isCreator = ref(false);
    let enableEdit = ref(false);
    const toggleEdit = () => {
      enableEdit.value = !enableEdit.value;
    };

    const tempActivity = Object.assign({}, props);
    let equipment = ref(props.equipment);

    const router = useRouter();
    const addEquipment = () => {
      router.push({
        name: "ActivityAddEquipment",
        params: { equipment: JSON.stringify(props.equipment) },
      });
    };

    const listenOnValueChange = (obj) => {
      if (obj.value === "") {
        return;
      }

      if (obj.attribute === "location") {
        tempActivity.location = obj.value;
      } else if (
        obj.attribute === "tags" &&
        !tempActivity.tags.find((item) => item.tag === obj.value.tag)
      ) {
        tempActivity.tags.push(obj.value);
      } else if (
        obj.attribute === "equipment" &&
        !equipment.value.find((item) => item === obj.value)
      ) {
        equipment.value.push(obj.value);
      } else if (obj.attribute in tempActivity) {
        tempActivity[obj.attribute] = obj.value;
      }
    };

    const listenOnRemoveTag = (tagObj) => {
      const index = tempActivity.tags.findIndex(
        (item) => item.tag === tagObj.tag
      );
      if (index > -1) {
        tempActivity.tags.splice(index, 1);
      }
    };

    const listenOnRemoveEquipment = (equipmentName) => {
      const index = equipment.value.findIndex((item) => item === equipmentName);
      if (index > -1) {
        equipment.value.splice(index, 1);
      }
    };

    const saveActivity = () => {
      let tempStart = new Date(tempActivity.start);
      let tempEnd = new Date(tempActivity.end);
      if (
        Date.now() > tempStart ||
        tempStart >= tempEnd ||
        tempEnd.getTime() - tempStart.getTime() > 604800000
      ) {
        msg.value = "Ugyldig dato (makslengde på aktivitet er én uke)";
        return;
      } else {
        emit("update:save", tempActivity);
        toggleEdit();
      }
    };

    const deleteActivity = () => {
      emit("delete:activity", tempActivity.id);
    };

    onMounted(() => {
      let email = window.localStorage.getItem("email");
      if (email !== null && email.localeCompare(props.createdBy.email) === 0) {
        isCreator.value = true;
      }
    });

    return {
      enableEdit,
      toggleEdit,
      saveActivity,
      deleteActivity,
      addEquipment,
      listenOnValueChange,
      listenOnRemoveTag,
      listenOnRemoveEquipment,
      isCreator,
      pop,
      mapPop,
      togglePop,
      msg,
    };
  },
};
</script>
<style scoped>
fieldset {
  padding: 16px;
  font-family: Arial, Helvetica, sans-serif;
  padding: 20px;
  gap: 10px;
  margin: 5px;
}
section {
  width: 100%;
}
h1 {
  font-size: 1em;
  margin-top: 3px;
  margin-bottom: 10px;
}

@media (max-width: 450px) {
  fieldset {
    flex-direction: column;
  }
  section {
    flex-direction: row;
  }
}
</style>
