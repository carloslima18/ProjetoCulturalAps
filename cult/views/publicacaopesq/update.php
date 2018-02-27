<?php

use yii\helpers\Html;

/* @var $this yii\web\View */
/* @var $model app\models\Publicacaopesq */

$this->title = 'Alterar pub. pesquisador: {nameAttribute}';
$this->params['breadcrumbs'][] = ['label' => 'Publicação pesquisador', 'url' => ['index']];
$this->params['breadcrumbs'][] = ['label' => $model->nome, 'url' => ['view', 'id' => $model->nome]];
$this->params['breadcrumbs'][] = 'Update';
?>
<div class="publicacaopesq-update">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
