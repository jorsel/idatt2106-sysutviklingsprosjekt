<template>
  <div class="main-wrapper">
    <v-calendar
      class="calendar"
      v-if="loaded"
      :isExpanded="true"
      title-position="left"
      :masks="masks"
      :attributes="attributes"
      :rows="2"
      @dayclick="dayClicked"
    >
    </v-calendar>
    <div class="text-center">
      <Label
        ><span class="dotRed"></span> = I Dag <span class="dotGreen"></span> =
        Opprettet <span class="dotBlue"></span> = Påmeldt</Label
      >
    </div>
    <div v-if="details" class="activity-wrapper">
      <div v-for="(day, index) in selectedDay.attributes" :key="index">
        <div v-if="day.key !== 'today'">
          <ActivityDetails
            :id="day.popover.id"
            :title="day.popover.label"
            :description="day.customData"
            :content="day.customData"
            :location="day.popover.location"
            :start="day.popover.startDate"
            :end="day.popover.endDate"
            :equipment="day.popover.equipment"
            :participants="day.popover.participants"
            :maxAmount="day.popover.maxAmount"
            :waitinglist="day.popover.waitingList"
            :intensity="day.popover.intensity"
            :tags="day.popover.tags"
            :tagList="day.popover.tagList"
            :createdBy="day.popover.createdBy"
            :isEditable="false"
          />
        </div>
      </div>
    </div>
    <div></div>
  </div>
</template>

<script>
import ActivityDetails from "@/components/activity/ActivityDetails.vue";
import { ref, onMounted } from "vue";
import { GET, baseURL, request } from "@/utility/request.js";
import Label from "../../components/common/Label.vue";

export default {
  name: "Calendar",
  components: {
    Label,
    ActivityDetails,
  },
  setup() {
    const selectedDay = ref({});
    const details = ref(false);

    const masks = {
      weekdays: "WWW",
    };

    const attributes = ref([
      {
        key: "today",
        highlight: "red",
        popover: {
          label: "Today",
        },
        customData: "",
        dates: new Date(),
      },
    ]);
    attributes.value.map((m) => ({
      key: "attribute.${m.id}",
      dot: "blue",
      dates: new Date(m.start),
      customData: m.title,
    }));

    const dayClicked = (day) => {
      if (day?.attributes) {
        selectedDay.value = day;
        details.value = true;
      }
    };

    const loaded = ref(false);

    onMounted(async () => {
      if (window.localStorage.getItem("firstName") !== null) {
        const joined = await request(
          `${baseURL}/profile/activities/upcoming`,
          GET
        );
        const created = await request(
          `${baseURL}/profile/activities/created`,
          GET
        );

        for (const j of joined.data) {
          if (j?.createdBy?.firstName) {
            attributes.value.push({
              key: j.id,
              highlight: "blue",
              popover: {
                id: j.id,
                label: j.title,
                location: j.location,
                startDate: new Date(j.start),
                endDate: new Date(j.end),
                equipment: j.equipment,
                participants: j.participants,
                maxAmount: j.maxAmount,
                waitingList: j.waitingList,
                intensity: j.intensity,
                tags: j.tags,
                tagList: [],
                createdBy: j.createdBy,
                highlight: true,
              },
              customData: j.description,
              dates: [{ start: new Date(j.start), end: new Date(j.end) }],
            });
          }
        }

        for (const k of created.data) {
          if (k?.createdBy?.firstName) {
            attributes.value.push({
              key: k.id,
              highlight: "green",
              popover: {
                id: k.id,
                label: k.title,
                location: k.location,
                startDate: new Date(k.start),
                endDate: new Date(k.end),
                equipment: k.equipment,
                participants: k.participants,
                maxAmount: k.maxAmount,
                waitingList: k.waitingList,
                intensity: k.intensity,
                tags: k.tags,
                tagList: [],
                createdBy: k.createdBy,
                highlight: true,
              },
              customData: k.description,
              dates: [{ start: new Date(k.start), end: new Date(k.end) }],
            });
          }
        }
      }
      loaded.value = true;
    });

    return {
      attributes,
      //eventsToday,
      masks,
      loaded,
      dayClicked,
      details,
      selectedDay,
    };
  },
};
</script>

<style scoped>
.main-wrapper {
  display: flex;
  flex-direction: column;
  margin-left: auto;
  margin-right: auto;
}

.calendar {
  width: 60vw;
}

.dotRed {
  height: 10px;
  width: 10px;
  background-color: red;
  border-radius: 50%;
  display: inline-block;
}

.dotBlue {
  height: 10px;
  width: 10px;
  background-color: blue;
  border-radius: 50%;
  display: inline-block;
}

.dotGreen {
  height: 10px;
  width: 10px;
  background-color: green;
  border-radius: 50%;
  display: inline-block;
}

.event-text {
  align-self: center;
  border: 2px solid;
}

.text-center {
  align-self: center;
  padding: 5px;
  border: 2px solid;
}

.title {
  border-bottom: 1px solid;
  padding-top: 3vh;
}

.activity-wrapper {
  width: 30vw;
  position: relative;
  align-self: center;
}

@media (max-width: 600px) {
  .calendar {
    width: 90vw;
  }
  .activity-wrapper {
    width: 80vw;
  }
}
</style>
