<template>
  <article class="editField">
    <label class="label" :for="id">{{ label }}:</label>
    <input
      :id="id"
      v-if="edit"
      :value="content"
      @input="updateContent"
      type="text"
      class="input is-small"
    />
    <p v-else :id="id">{{ content }}</p>
  </article>
</template>

<style scoped>
article {
  display: block;
}
</style>

<script>
export default {
  name: "ActivityFieldText",

  props: {
    id: {
      type: String,
      required: true,
    },
    content: {
      type: String,
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
    return { updateContent };
  },
};
</script>
