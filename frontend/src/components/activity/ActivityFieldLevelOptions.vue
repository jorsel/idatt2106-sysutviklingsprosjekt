<template>
  <article class="editField">
    <div>
      <label class="label" :for="id">{{ label }}:</label>
      <p v-show="!edit" :id="id">{{ getIntensity }}</p>
    </div>

    <p class="control" v-show="edit">
      <span class="select">
        <select :value="content" :id="id" @input="updateContent">
          <option selected value="1">Lav</option>
          <option value="2">Medium</option>
          <option value="3">Høy</option>
        </select>
      </span>
    </p>
  </article>
</template>

<style scoped>
article {
  display: block;
}
.select {
  width: 100%;
}
select {
  width: inherit;
}
label {
  padding-right: 5px;
}
div {
  display: flex;
  flex-direction: row;
}
</style>

<script>
import { computed } from "vue";

export default {
  name: "ActivityFieldLevelOptions",
  props: {
    id: {
      type: String,
      required: true,
    },
    content: {
      type: Number,
      required: true,
    },
    label: {
      type: String,
      required: true,
    },
    edit: {
      type: Boolean,
      required: false,
    },
  },
  setup(props, { emit }) {
    const updateContent = (event) => {
      const data = { attribute: `${props.id}`, value: event.target.value };
      emit(`update:${props.id}`, data);
    };

    const getIntensity = computed(() => {
      switch (props.content) {
        case 1:
          return "Lavt";
        case 2:
          return "Medium";
        case 3:
          return "Høyt";
        default:
          return "-";
      }
    });

    return {
      updateContent,
      getIntensity,
    };
  },
};
</script>
