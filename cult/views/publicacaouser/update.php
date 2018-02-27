<?php

use yii\helpers\Html;

/* @var $this yii\web\View */
/* @var $model app\models\Publicacaouser */

$this->title = 'Alterar pub. usuário: {nameAttribute}';
$this->params['breadcrumbs'][] = ['label' => 'Publicação usuário', 'url' => ['index']];
$this->params['breadcrumbs'][] = ['label' => $model->id, 'url' => ['view', 'id' => $model->nome]];
$this->params['breadcrumbs'][] = 'Update';
?>
<div class="publicacaouser-update">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
