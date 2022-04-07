<template>
  <section class="profile box">
    <form @submit.prevent="submit" id="profileForm">
      <h1 class="title is-4">Endre profil</h1>

      <div class="notification is-danger" v-show="msg">
        <Button @click="hideNotification" class="delete is-small" />
        {{ msg }}
      </div>

      <article class="field">
        <!-- <Label :id="'email'">E-post:</Label>
        <Input
          :type="'email'"
          :id="'email'"
          :required="required"
          :value="email"
          v-model="email"
        >
          <template v-slot:icon>
            <em class="fas fa-envelope"></em>
          </template>
        </Input> -->

        <Label :id="'firstname'">Fornavn:</Label>
        <Input
          :type="'text'"
          :id="'firstname'"
          :required="required"
          :value="firstName"
          v-model="firstName"
        >
          <template v-slot:icon>
            <em class="fas fa-user"></em>
          </template>
        </Input>

        <Label :id="'lastname'">Etternavn:</Label>
        <Input
          :type="'text'"
          :id="'lastname'"
          :required="required"
          :value="lastName"
          v-model="lastName"
        >
          <template v-slot:icon>
            <em class="fas fa-user"></em>
          </template>
        </Input>

        <Label :id="'dateOfBirth'">Fødselsdag:</Label>
        <v-date-picker v-model="dateOfBirth" mode="date" :timezone="''" />

        <Label :id="'intensity'">Aktivitetsnivå:</Label>
        <Select
          :id="'intensity'"
          :required="required"
          :options="options"
          :value="intensity"
          v-model="intensity"
        >
          <template v-slot:icon>
            <em class="fas fa-heartbeat"></em>
          </template>
        </Select>

        <Label :id="'public'">Offentlig konto?:</Label>
        <Input
          class="checkbox"
          :type="'checkbox'"
          :id="'public'"
          :value="publicCheck"
          v-model="publicCheck"
        />
      </article>

      <Button :type="'submit'">Lagre</Button>
      <Button @click.prevent="cancel">Avbryt</Button>
    </form>

    <Modal>
      <template v-slot:btnTitle>Bytt passord</template>
      <form @submit.prevent="changePw">
        <div class="mainWrapper">
          <section class="profile">
            <h1>Bytt passord</h1>
            <br />

            <Label :id="'oldPassword'">Gammelt passord:</Label>
            <Input
              :type="'password'"
              :id="'oldPassword'"
              :required="required"
              v-model="oldPassword"
            >
              <template v-slot:icon>
                <em class="fas fa-lock"></em>
              </template>
            </Input>
            <br />
            <Label :id="'newPassword'">Nytt passord:</Label>
            <Input
              :type="'password'"
              :id="'newPassword'"
              :required="required"
              v-model="newPassword"
            >
              <template v-slot:icon>
                <em class="fas fa-lock"></em>
              </template>
            </Input>
            <br />
            <Label :id="'repeatPassword'">Gjenta nytt passord:</Label>
            <Input
              :type="'password'"
              :id="'repeatPassword'"
              :required="required"
              v-model="newPasswordRe"
            >
              <template v-slot:icon>
                <em class="fas fa-lock"></em>
              </template>
            </Input>

            <Button :type="'submit'">Submit</Button>

            <p v-show="PwNotRepeated">
              Passordene er ikke like, vennligst prøv på nytt
            </p>
            <p v-show="isFilled">Vennligst fyll ut alle feltene</p>
          </section>
        </div>
      </form>
    </Modal>
  </section>
</template>

<script>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { PUT, baseURL, request } from "@/utility/request.js";
import Modal from "@/components/Modal.vue";
import Button from "@/components/common/Button.vue";
import Input from "@/components/common/Input.vue";
import Label from "@/components/common/Label.vue";
import Select from "@/components/common/Select.vue";

export default {
  name: "userProfileEdit",
  components: { Button, Modal, Input, Label, Select },
  setup() {
    const myStorage = window.localStorage;

    const firstName = ref(myStorage.getItem("firstName"));
    const lastName = ref(myStorage.getItem("lastName"));
    const email = ref(myStorage.getItem("email"));
    const publicCheck = ref(myStorage.getItem("public"));
    const dateOfBirth = ref(myStorage.getItem("dateOfBirth"));
    if (dateOfBirth.value == null || dateOfBirth.value == "null") {
      dateOfBirth.value = new Date();
    }
    const intensity = ref(myStorage.getItem("intensity"));

    const oldPassword = ref("");
    const newPassword = ref("");
    const newPasswordRe = ref("");
    const PwNotRepeated = ref(false);
    const isFilled = ref(false);

    const required = ref(true);
    const msg = ref("");

    const hideNotification = () => {
      msg.value = "";
    };

    const options = ref([
      { key: "Lav", value: 1 },
      { key: "Medium", value: 2 },
      { key: "Høy", value: 3 },
    ]);

    const router = useRouter();

    const changePw = async () => {
      if (
        oldPassword.value === "" ||
        newPassword.value === "" ||
        newPasswordRe.value === ""
      ) {
        isFilled.value = true;
        return;
      } else {
        isFilled.value = false;
      }
      if (newPassword.value !== newPasswordRe.value) {
        PwNotRepeated.value = true;
        return;
      }

      const response = await request(
        `${baseURL}/profile/newPassword`,
        PUT,
        JSON.stringify({
          oldPassword: oldPassword.value,
          newPassword: newPassword.value,
        })
      );

      if (response.status !== 200) {
        console.error("An error occured when updating password!");
        msg.value =
          "En feil oppstod som forhindrer profilen i å bli oppdattert.";
      }

      oldPassword.value = "";
      newPassword.value = "";
      newPasswordRe.value = "";
    };

    const submit = async () => {
      const response = await request(
        `${baseURL}/profile`,
        PUT,
        JSON.stringify({
          firstName: firstName.value,
          lastName: lastName.value,
          dateOfBirth: new Date(dateOfBirth.value).toISOString(),
          // email: email.value,
          public: publicCheck.value,
          intensity: intensity.value,
        })
      );

      if (response.status === 200) {
        if (response.data.firstName) {
          for (const [key, value] of Object.entries(response.data)) {
            if (key === "profilePic" && value?.content) {
              myStorage.setItem(key, "data:image/jpeg;base64," + value.content);
            } else if (key === "profilePicThumbnail" && value?.content) {
              myStorage.setItem(key, "data:image/jpeg;base64," + value.content);
            } else {
              myStorage.setItem(key, value);
            }
          }
        }

        router.push({
          name: "Profile",
          params: { msg: "Din profil ble oppdatert" },
        });
      } else {
        msg.value = "En feil oppstod og kunne ikke hente profil informasjon.";
        console.error("An error occured when updating profile!");
      }
    };
    const cancel = () => {
      router.push({ name: "Profile" });
    };

    return {
      firstName,
      lastName,
      dateOfBirth,
      intensity,
      email,
      publicCheck,
      options,
      required,
      oldPassword,
      newPassword,
      newPasswordRe,
      PwNotRepeated,
      isFilled,
      changePw,
      submit,
      cancel,
      msg,
      hideNotification,
    };
  },
};
</script>

<style scoped>
.profile {
  margin-left: auto;
  margin-right: auto;
}
</style>
