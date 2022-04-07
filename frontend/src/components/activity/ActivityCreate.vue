<template>
  <section id="section">
    <div class="box" id="box">
      <ActivityFieldText
        :id="'title'"
        :content="title"
        :label="'Tittel'"
        :edit="true"
        @update:title="listenOnValueChange"
      />
      <ActivityFieldTextArea
        :id="'description'"
        :content="description"
        :label="'Beskrivelse'"
        :edit="true"
        @update:description="listenOnValueChange"
      />

      <ActivityFieldLocation
        :id="'location'"
        :content="location"
        :label="'Adresse'"
        :edit="true"
        @update:location="listenOnValueChange"
      />
      <ActivityFieldDate
        :id="'start'"
        :content="start"
        :label="'Startdato'"
        :edit="true"
        @update:start="listenOnValueChange"
      />
      <ActivityFieldDate
        :id="'end'"
        :content="end"
        :label="'Sluttdato'"
        :edit="true"
        @update:end="listenOnValueChange"
      />
      <ActivityFieldLevelOptions
        :id="'intensity'"
        :content="intensity"
        :label="'Aktivitetsnivå'"
        :edit="true"
        @update:intensity="listenOnValueChange"
      />
      <ActivityFieldEquipment
        :id="'equipment'"
        :content="equipment"
        :label="'Utstyr'"
        :edit="true"
        @update:equipment="listenOnValueChange"
        @update:removeEquipment="listenOnRemoveEquipment"
      />
      <ActivityFieldNumber
        :id="'maxAmount'"
        :content="maxAmount"
        :label="'Maks påmeldte'"
        :edit="true"
        @update:maxAmount="listenOnValueChange"
      />
      <ActivityFieldTags
        :id="'tags'"
        :content="tags"
        :label="'Tags'"
        :tagList="tagList"
        :edit="true"
        @update:tags="listenOnValueChange"
        @update:removeTag="listenOnRemoveTag"
      />
      <br />
      <input
        type="submit"
        id="opprettbtn"
        @click.prevent="createActivity"
        value="Opprett"
      />
      <input
        type="button"
        id="avbrytbtn"
        @click.prevent="cancelCreateActivity"
        value="Avbryt"
      />
    </div>
  </section>
</template>

<script>
import ActivityFieldText from "@/components/activity/ActivityFieldText.vue";
import ActivityFieldNumber from "@/components/activity/ActivityFieldNumber.vue";
import ActivityFieldDate from "@/components/activity/ActivityFieldDate.vue";
import ActivityFieldEquipment from "@/components/activity/ActivityFieldEquipment.vue";
import ActivityFieldTags from "@/components/activity/ActivityFieldTags.vue";
import ActivityFieldLevelOptions from "@/components/activity/ActivityFieldLevelOptions.vue";
import ActivityFieldTextArea from "@/components/activity/ActivityFieldTextArea.vue";

import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import ActivityFieldLocation from "@/components/activity/ActivityFieldLocation";

export default {
  name: "ActivityCreate",
  components: {
    ActivityFieldText,
    ActivityFieldNumber,
    ActivityFieldDate,
    ActivityFieldEquipment,
    ActivityFieldTags,
    ActivityFieldLevelOptions,
    ActivityFieldTextArea,
    ActivityFieldLocation,
  },
  props: {
    tagList: {
      type: Array,
      required: true,
    },
  },
  setup(props, { emit }) {
    let createdBy = reactive({
      firstName: "nisse",
      lastName: "test",
      dateOfBirth: "2021-04-20T06:46:35.431Z",
    });
    let title = ref("");
    let description = ref("");
    let location = reactive({
      address: "",
      longitude: 0,
      latitude: 0,
    });
    let end = ref(new Date());
    let start = ref(new Date());
    let equipment = ref([]);
    let intensity = ref(1);
    let maxAmount = ref(1);
    let tags = ref([]);

    const tempActivity = {
      // createdBy: createdBy,
      title: title.value,
      description: description.value,
      location: location,
      start: start.value,
      end: end.value,
      equipment: equipment.value,
      intensity: intensity.value,
      maxAmount: maxAmount.value,
      tags: tags.value,
    };

    const listenOnValueChange = (obj) => {
      if (obj.value === "") {
        return;
      }

      if (obj.attribute === "location") {
        tempActivity.location = obj.value;
      } else if (
        obj.attribute === "tags" &&
        !tags.value.find((item) => item.tag === obj.value.tag)
      ) {
        tags.value.push(obj.value);
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
      const index = tags.value.findIndex((item) => item.tag === tagObj.tag);
      if (index > -1) {
        tags.value.splice(index, 1);
      }
    };

    const listenOnRemoveEquipment = (equipmentName) => {
      const index = equipment.value.findIndex((item) => item === equipmentName);
      if (index > -1) {
        equipment.value.splice(index, 1);
      }
    };

    const createActivity = () => {
      emit("update:create", tempActivity);
    };

    const router = useRouter();
    const cancelCreateActivity = () => router.go(-1);

    return {
      createdBy,
      title,
      description,
      location,
      start,
      end,
      equipment,
      intensity,
      maxAmount,
      tags,
      createActivity,
      cancelCreateActivity,
      listenOnValueChange,
      listenOnRemoveTag,
      listenOnRemoveEquipment,
    };
  },
};
</script>

<style scoped>
#section {
  width: 100%;
}

input {
  width: 100%;
  padding: 12px 20px;
  margin: 8px 0;
}

select {
  width: 90%;
  padding: 12px 20px;
  margin: 8px 0;
}
div * {
  margin: 2px;
}
input {
  padding: 8px 14px;
  cursor: pointer;
  border-width: 1px;
  border-radius: 2px;
  font-size: 14px;
  font-weight: 400;
  -webkit-box-shadow: 0px 10px 20px -6px rgba(0, 0, 0, 0.12);
  -moz-box-shadow: 0px 10px 20px -6px rgba(0, 0, 0, 0.12);
  box-shadow: 0px 10px 20px -6px rgba(0, 0, 0, 0.12);
  background: #ed1c24;
  color: white;
}
</style>
