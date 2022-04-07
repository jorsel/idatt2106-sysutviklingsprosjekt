<template>
  <div id="feedback-main" class="box">
    <section class="feedback">
      <img alt="Gidd logo" id="logo" src="@/assets/3-nobackground.png" />

      <div
        class="notification"
        v-show="msg"
        :class="success ? 'is-success' : 'is-danger'"
      >
        <button @click="hideNotification" class="delete is-small" />
        {{ msg }}
      </div>

      <div id="rating-area">
        <p>Vennligst del hva du synes om produktet vårt</p>
        <form @submit.prevent="onSubmit">
          <div class="field is-grouped">
            <div class="control">
              <Label class="radio" :id="'box1'">
                1
                <Input
                  :type="'radio'"
                  :name="'select'"
                  @update:radio="getSelected"
                  :id="'box1'"
                  :value="'1'"
                />
              </Label>
            </div>

            <div class="control">
              <Label class="radio" :id="'box2'">
                2
                <Input
                  :type="'radio'"
                  :name="'select'"
                  @update:radio="getSelected"
                  :id="'box2'"
                  :value="2"
                />
              </Label>
            </div>

            <div class="control">
              <Label class="radio" :id="'box3'">
                3
                <Input
                  :type="'radio'"
                  :name="'select'"
                  @update:radio="getSelected"
                  :id="'box3'"
                  :value="3"
                />
              </Label>
            </div>

            <div class="control">
              <Label class="radio" :id="'box4'">
                4
                <Input
                  :type="'radio'"
                  :name="'select'"
                  @update:radio="getSelected"
                  :id="'box4'"
                  :value="4"
                />
              </Label>
            </div>

            <div class="control">
              <Label class="radio" :id="'box5'">
                5
                <Input
                  :type="'radio'"
                  :name="'select'"
                  @update:radio="getSelected"
                  :id="'box5'"
                  :value="5"
                />
              </Label>
            </div>
          </div>

          <br />
          <div>
            <Label :id="'content'">Noe spesifikt du ønsker å si oss?</Label>
            <TextArea :id="'content'" v-model="feedback.content" />
          </div>
          <Button class="button" :type="'submit'">Submit</Button>
        </form>
      </div>
    </section>
  </div>
</template>

<script>
import { POST, baseURL, request } from "@/utility/request.js";
import { reactive, ref } from "vue";

import Button from "@/components/common/Button.vue";
import Input from "@/components/common/Input.vue";
import Label from "@/components/common/Label.vue";
import TextArea from "@/components/common/TextArea.vue";

export default {
  name: "Feedback",
  components: { Button, Label, Input, TextArea },
  setup() {
    let feedback = reactive({
      rating: "",
      content: "",
    });

    const success = ref(true);
    const msg = ref("");

    const hideNotification = () => {
      success.value = true;
      msg.value = "";
    };

    const getSelected = (event) => {
      feedback.rating = event.toString();
    };

    const onSubmit = async () => {
      try {
        const response = await request(
          `${baseURL}/feedback`,
          POST,
          JSON.stringify(feedback)
        );

        if (response.status === 200) {
          Object.assign(feedback, {
            rating: "",
            content: "",
          });
          msg.value = "Tilbakemeldingen ble send!";
        } else {
          console.error("An error occured while sending feedback");

          success.value = false;
          msg.value = "En feil oppstod og tilbakemeldingen ble ikke sendt!";
        }
      } catch {
        console.error("An error occured while sending feedback");
        success.value = false;
        msg.value = "En feil oppstod og tilbakemeldingen ble ikke sendt!";
      }
    };

    return {
      feedback,
      getSelected,
      onSubmit,
      success,
      msg,
      hideNotification,
    };
  },
};
</script>

<style scoped>
#feedback-main {
  margin-left: auto;
  margin-right: auto;
}

#logo {
  height: 100px;
  width: auto;
  margin: auto;
}
</style>
