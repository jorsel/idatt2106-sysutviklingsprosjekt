<template>
  <article id="wrap">
    <div class="editField">
      <div class="in">
        <label class="label" :for="id">{{ label }}:</label>
        <input
          class="input is-small"
          :id="id"
          v-if="edit"
          type="text"
          v-model="tempInput"
          placeholder="Skriv inn utstyr her"
        />
        <button id="addbtn" class="button is-small" v-if="edit" @click="addReq">
          Add
        </button>
      </div>
    </div>

    <div class="tags">
      <div :id="id" v-for="(equipment, index) in content" :key="index">
        <ActivityEquipment
          :edit="edit"
          :equipmentName="equipment"
          @update:remove="listenOnRemoveEquipment"
        />
      </div>
    </div>
  </article>
</template>



<script>
import { ref } from "vue";
import ActivityEquipment from "@/components/activity/ActivityEquipment.vue";
export default {
  name: "ActivityFieldEquipment",

  components: {
    ActivityEquipment,
  },

  props: {
    id: {
      type: String,
      required: true,
    },
    content: {
      type: Array,
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
    let tempInput = ref("");

    const addReq = () => {
      const data = { attribute: `${props.id}`, value: tempInput.value };
      emit(`update:${props.id}`, data);
      tempInput.value = "";
    };
    const listenOnRemoveEquipment = (equipmentName) => {
      emit("update:removeEquipment", equipmentName);
    };

    return {
      addReq,
      tempInput,
      listenOnRemoveEquipment,
    };
  },
};
</script>

<style scoped>
#addbtn {
  background-color: #ed1c24;
  color: #fff;
}

#wrap {
  display: flex;
  flex-direction: column;
}
.tags {
  display: flex;
  flex-direction: row;
  gap: 5px;
}
.in {
  width: 100%;
}
</style>
