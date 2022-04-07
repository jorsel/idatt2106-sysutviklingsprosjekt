<template>
  <article class="editField">
    <label class="label" :for="id" v-if="edit">{{ label }}:</label>
    <v-date-picker v-if="edit" v-model="date" mode="dateTime" is24hr>
      <template v-slot="{ inputValue, inputEvents }">
        <input
          :id="id"
          class="input is-small px-2 py-1 border rounded focus:outline-none focus:border-blue-300"
          :value="inputValue"
          v-on="inputEvents"
        />
      </template>
    </v-date-picker>

    <!---
      <input
        :id="id"
        v-if="edit"
        :value="content"
        @input="updateValue"
        type="datetime-local"
        />
       --->

    <div v-else class="time">
      <label class="label">{{ label }}:</label>
      <p>
        {{ ("0" + content.getHours()).slice(-2) }}:{{
          ("0" + content.getMinutes()).slice(-2)
        }}
        - {{ content.getDate() }}/{{ content.getMonth() + 1 }}/{{
          content.getFullYear()
        }}
      </p>
    </div>
  </article>
</template>

<style scoped>
.time {
  display: flex;
  flex-direction: row;
  width: 100%;
}
.editField {
  flex-direction: column;
}
label {
  padding-right: 5px;
}
</style>

<script>
import { ref, onUpdated } from "vue";
export default {
  name: "ActivityFieldDate",
  props: {
    id: {
      type: String,
      required: true,
    },
    content: {
      type: Date,
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
    const date = ref(new Date(props.content));

    const getTiming = (da) => {
      if (da !== null) {
        if (
          (da.getMonth() > 2 && da.getMonth() < 9) ||
          (da.getMonth() === 2 && da.getDate() > 28) ||
          (da.getMonth() === 9 && da.getDate() < 31)
        ) {
          return new Date(da.setHours(da.getHours() + 2));
        } else {
          return new Date(da.setHours(da.getHours() + 1));
        }
      } else {
        date.value = new Date(props.content);
        return new Date(date.value);
      }
    };

    onUpdated(() => {
      if (date.value !== null) {
        if (props.edit) {
          const data = {
            attribute: `${props.id}`,
            value: getTiming(date.value).toISOString(),
          };
          emit(`update:${props.id}`, data);
        }
      } else {
        return;
      }
    });

    return { date };
  },
};
</script>
