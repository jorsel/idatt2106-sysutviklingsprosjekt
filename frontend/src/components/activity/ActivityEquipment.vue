<template>
  <span class="tag">
    {{ equipmentName }}
    <button
      class="delete is-medium"
      v-if="edit"
      @click="removeEquipment"
    ></button>
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
  name: "ActivityEquipment",

  props: {
    equipmentName: {
      type: String,
      required: true,
    },
    edit: {
      type: Boolean,
      required: true,
    },
  },
  setup(props, { emit }) {
    const removeEquipment = () => {
      emit("update:remove", props.equipmentName);
    };

    return { removeEquipment };
  },
};
</script>
