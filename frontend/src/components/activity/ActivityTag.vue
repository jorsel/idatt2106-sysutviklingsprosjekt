<template>
  <span class="tag">
    {{ tagName }}
    <button class="delete is-medium" v-if="edit" @click="removeTag"></button>
  </span>
</template>

<style scoped>
.tag {
  background-color: #ed1c24;
  color: white;
}
</style>

<script>
export default {
  name: "ActivityTag",

  props: {
    tagName: {
      type: String,
      required: true,
    },
    edit: {
      type: Boolean,
      required: true,
    },
  },
  setup(props, { emit }) {
    const removeTag = () => {
      emit("update:remove", { tag: props.tagName });
    };

    return { removeTag };
  },
};
</script>
