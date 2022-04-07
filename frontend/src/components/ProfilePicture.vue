<template>
  <div v-if="msg" class="notification is-danger">
    {{ msg }}
  </div>

  <figure v-if="previewReady" class="image is-5by3">
    <img :src="previewImage.result" alt="Image preview" />
  </figure>
  <div class="file has-name is-boxed">
    <label class="file-label">
      <input class="file-input" @change="onImageUpload" type="file" />
      <span class="file-cta">
        <span class="file-icon">
          <i class="fas fa-upload"></i>
        </span>
        <span class="file-label"> Velg et bilde.. </span>
      </span>
      <span v-if="fileName" class="file-name preview-name">
        {{ fileName }}
      </span>
    </label>
  </div>

  <button type="button" @click="startUpload">Last opp</button>
</template>

<script>
import { PUT, baseURL, request } from "@/utility/request.js";
import { ref } from "vue";

export default {
  name: "profilePicture",
  setup(props, { emit }) {
    const myStorage = window.localStorage;
    const msg = ref("");

    const formData = new FormData();
    const fileName = ref("");
    const previewImage = ref(new FileReader());
    const previewReady = ref(false);
    previewImage.value.onload = () => {
      previewReady.value = true;
    };

    const onImageUpload = (event) => {
      previewReady.value = false;
      const file = event.target.files[0];
      fileName.value = file.name;
      previewImage.value.readAsDataURL(file);
      formData.set("file", file);
    };

    const startUpload = async () => {
      const response = await request(
        `${baseURL}/profile/picture`,
        PUT,
        formData
      );

      if (response.status === 200) {
        if (response.data.firstName) {
          for (const [key, value] of Object.entries(response.data)) {
            if (key === "profilePic" && value?.content) {
              myStorage.setItem(key, "data:image/jpeg;base64," + value.content);
            } else if (key === "profilePicThumbnail" && value?.content) {
              myStorage.setItem(key, "data:image/jpeg;base64," + value.content);
            }
          }
        }
        emit("update:picture");
      } else {
        console.error("An error occured while uploading an image!");
        msg.value = "Kunne ikke laste opp bilde";
      }
    };
    return {
      onImageUpload,
      startUpload,
      fileName,
      previewImage,
      previewReady,
      msg,
    };
  },
};
</script>

<style scoped>
.preview-name {
  background-color: gray;
}
/* .custom-file-label {
  display: flex;
}

.custom-file-input {
  display: flex;
  flex-direction: column;
}

.pictureWrapper {
  display: flex;
  flex-direction: column;
}

.custom-file {
  display: flex;
  position: absolute;
}

.btn-success {
  margin-top: 25px;
} */
</style>
