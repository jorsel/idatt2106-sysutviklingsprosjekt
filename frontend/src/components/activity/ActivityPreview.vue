<template>
  <section class="box">
    <fieldset>
      <h1 class="title is-4">{{ title }}</h1>
      <div id="descfield">
        <ActivityFieldTextArea
          :id="'description'"
          :content="description"
          :label="'Beskrivelse'"
        />
      </div>

      <div id="datefield">
        <ActivityFieldDate
          :id="'start'"
          :content="start"
          :label="'Start-tidpunkt'"
        />
      </div>

      <div class="prev">
        <label class="label" id="addressfield">Adresse: </label>
        <p>{{ location.address }}</p>
      </div>

      <div class="prev">
        <label class="label" id="addressfield">Deltakere: </label>
        <p>{{ participants.length }}/{{ maxAmount }}</p>
      </div>

      <ActivityFieldLevelOptions
        :id="'intensity'"
        :content="intensity"
        :label="'Aktivitetsnivå'"
      />

      <ActivityFieldTags
        :id="'tags'"
        :content="tags"
        :tagList="[]"
        :label="'Tags'"
        @tag:clicked="emitClicked"
      />
    </fieldset>
  </section>
</template>

<script>
import ActivityFieldLevelOptions from "@/components/activity/ActivityFieldLevelOptions.vue";
import ActivityFieldTags from "@/components/activity/ActivityFieldTags.vue";
import ActivityFieldDate from "@/components/activity/ActivityFieldDate.vue";
import ActivityFieldTextArea from "@/components/activity/ActivityFieldTextArea.vue";

export default {
  name: "ActivityPreview",
  components: {
    ActivityFieldTextArea,
    ActivityFieldLevelOptions,
    ActivityFieldTags,
    ActivityFieldDate,
  },
  props: {
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
  },
  setup(props, { emit }) {
    const emitClicked = (clickedTag) => {
      emit("tag:clicked", { tag: clickedTag.tag });
    };
    return { emitClicked };
  },
};
</script>
<style scoped>
fieldset {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 5px;
  font-family: Arial, Helvetica, sans-serif;
  margin: 5px;
  gap: 10px;
}

fieldset > * {
  padding: 1px;
}

section {
  width: 100%;
  padding: 10px;
}

h1 {
  font-size: 1em;
  margin-top: 3px;
  margin-bottom: 10px;
}

article {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 5px;
}
.prev {
  display: flex;
  flex-direction: row;
}
label {
  padding-right: 5px;
}
@media (max-width: 450px) {
  #descfield {
    display: none;
  }
  #datefield {
    display: none;
  }
  #addressfield {
    display: none;
  }
}
</style>
